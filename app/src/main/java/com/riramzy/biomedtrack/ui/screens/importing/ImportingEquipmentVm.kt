package com.riramzy.biomedtrack.ui.screens.importing

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import com.riramzy.biomedtrack.domain.usecase.department.GetAllDepartmentsOnceUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.ExcelParserUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.RawEquipment
import com.riramzy.biomedtrack.ui.components.importing.DataPreviewRow
import com.riramzy.biomedtrack.ui.components.importing.ImportLog
import com.riramzy.biomedtrack.ui.components.importing.LogType
import com.riramzy.biomedtrack.ui.components.importing.ValidationStatus
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.FileExportHelper
import com.riramzy.biomedtrack.utils.Timestamps.parseDateToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class ImportingUiState {
    object Idle: ImportingUiState()
    object Parsing: ImportingUiState()
    data class Importing(
        val progress: Float,
        val currentChunk: Int,
        val totalChunks: Int,
        val liveLogs: List<ImportLog>
    ): ImportingUiState()
    object CreatingDepartments: ImportingUiState()
    data class PreviewReady(
        val previewRows: List<DataPreviewRow>,
        val totalValidRows: Int,
        val selectedCount: Int
    ): ImportingUiState()
    data class Success(
        val importedCount: Int,
        val skippedCount: Int,
        val failedCount: Int
    ): ImportingUiState()
    data class Error(val message: String): ImportingUiState()
}

data class UploadedFile(
    val uri: Uri,
    val fileName: String,
    val fileSize: Long
)

@HiltViewModel
class ImportingEquipmentVm @Inject constructor(
    application: Application,
    private val excelParserUseCase: ExcelParserUseCase,
    private val getAllDepartmentsOnceUseCase: GetAllDepartmentsOnceUseCase,
    private val equipmentRepo: EquipmentRepo,
    private val departmentRepo: DepartmentRepo,
    private val sessionManager: SessionManager
): AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<ImportingUiState>(ImportingUiState.Idle)
    val uiState: StateFlow<ImportingUiState> = _uiState.asStateFlow()

    private var cachedRawEquipment = emptyList<RawEquipment>()
    private var cachedUniqueDepartments = emptySet<String>()

    private val _uploadHistory = MutableStateFlow<List<UploadedFile>>(emptyList())
    val uploadHistory: StateFlow<List<UploadedFile>> = _uploadHistory.asStateFlow()

    private var _selectedFile = MutableStateFlow<UploadedFile?>(null)
    val selectedFile: StateFlow<UploadedFile?> = _selectedFile.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()
    val currentUser = sessionManager.currentUser

    fun parseExcelFile(uri: Uri) {
        _uiState.value = ImportingUiState.Parsing
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingEquipment = equipmentRepo.getAllEquipment().first()
                val existingEquipmentSerialNumbers = existingEquipment.map { it.serialNumber }.toSet()

                val inputStream = getApplication<Application>().contentResolver.openInputStream(uri) ?: throw Exception("Failed to open input stream")

                val (previewRows, rawEquipment, uniqueDepartments) = excelParserUseCase(inputStream, existingEquipmentSerialNumbers)

                val previewRowsWithSelection = previewRows.map {
                    it.copy(isSelected = it.validationStatus == ValidationStatus.VALID)
                }

                cachedRawEquipment = rawEquipment
                cachedUniqueDepartments = uniqueDepartments

                _uiState.value = ImportingUiState.PreviewReady(
                    previewRows = previewRowsWithSelection,
                    totalValidRows = rawEquipment.size,
                    selectedCount = rawEquipment.size
                )

                _snackbarEvent.emit("Excel file parsed successfully")
            } catch (e: Exception) {
                _uiState.value = ImportingUiState.Error(e.message ?: "Failed to parse Excel file")
                _snackbarEvent.emit(e.message ?: "Failed to parse Excel file")
            }
        }
    }

    fun startImporting() {
        val currentState = _uiState.value as? ImportingUiState.PreviewReady ?: return

        val selectedSerials = currentState.previewRows
            .filter { it.isSelected }
            .map { it.serialNumber }
            .toSet()

        val equipmentToImport = cachedRawEquipment
            .filter { selectedSerials.contains(it.serialNumber) }

        if (equipmentToImport.isEmpty()) {
            viewModelScope.launch { _snackbarEvent.emit("No equipment selected to import") }
            return
        }

        if (cachedRawEquipment.isEmpty()) {
            _uiState.value = ImportingUiState.Error("No valid equipment to import")
            viewModelScope.launch { _snackbarEvent.emit("No valid equipment to import") }
            return
        }

        val currentLogs = mutableListOf<ImportLog>()
        val addLog = { message: String, type: LogType ->
            currentLogs.add(0, ImportLog(message, type))
        }

        val skippedCount = cachedRawEquipment.size - equipmentToImport.size

        viewModelScope.launch(Dispatchers.IO) {
            var totalFailed = 0
            var totalImported = 0

            try {
                _uiState.value = ImportingUiState.CreatingDepartments
                addLog("Creating departments...", LogType.SUCCESS)

                val existingDepartments = departmentRepo.getAllDepartmentsOnce()
                val existingDepartmentsNames = existingDepartments
                    .map { it.name.trim().lowercase() }
                    .toSet()

                val missingDepartments = cachedUniqueDepartments
                    .map { it.trim() }
                    .distinctBy { it.lowercase() }
                    .filter { deptName ->
                        !existingDepartmentsNames.contains(deptName.lowercase())
                    }

                if (missingDepartments.isNotEmpty()) {
                    addLog("Created ${missingDepartments.size} new departments", LogType.SUCCESS)
                }

                for (deptName in missingDepartments) {
                    val newDepartment = Department(
                        id = UUID.randomUUID().toString(),
                        name = deptName,
                        totalEquipment = 0,
                        dueServiceEquipment = 0,
                        downEquipment = 0
                    )

                    departmentRepo.addDepartment(newDepartment)
                }

                val finalDepartments = getAllDepartmentsOnceUseCase()
                val departmentsMap = finalDepartments.associateBy { it.name.trim().lowercase() }

                val currentUserId = sessionManager.currentUser.value?.id ?: throw Exception("User not logged in")

                val equipmentListToImport = equipmentToImport.map { raw ->
                    val matchedDepartments = departmentsMap[raw.departmentName.trim().lowercase()] ?: throw Exception("Department not found")

                    Equipment(
                        id = UUID.randomUUID().toString(),
                        name = raw.name,
                        model = raw.model,
                        serialNumber = raw.serialNumber,
                        manufacturer = raw.manufacturer,
                        agent = raw.agent.ifBlank { "None" },
                        category = raw.category,
                        department = matchedDepartments,
                        location = raw.location.ifBlank { matchedDepartments.name },
                        status = try {
                            EquipmentStatus.valueOf(raw.status.uppercase())
                        } catch (e: Exception) {
                            EquipmentStatus.ONLINE
                        },
                        installDate = parseDateToLong(raw.installDate),
                        warrantyEndDate = parseDateToLong(raw.warrantyEndDate),
                        createdBy = currentUserId,
                        serviceIntervalDays = 180,
                        contractInfo = if (raw.contractBy.isNotBlank() && raw.contractEndDate.isNotBlank()) {
                            "${raw.contractBy} - Ends: ${raw.contractEndDate}"
                        } else null,
                        assignedTo = null
                    )
                }

                val chunks = equipmentListToImport.chunked(500)
                val totalChunks = chunks.size

                _uiState.value = ImportingUiState.Importing(
                    progress = 0f,
                    currentChunk = 0,
                    totalChunks = totalChunks,
                    liveLogs = currentLogs.toList()
                )

                for ((index, chunk) in chunks.withIndex()) {
                    try {
                        addLog("Saving batch ${index + 1} of $totalChunks (${chunk.size} items)", LogType.SUCCESS)

                        val currentProgress = index.toFloat() / totalChunks.toFloat()

                        _uiState.value = ImportingUiState.Importing(
                            progress = currentProgress,
                            currentChunk = index + 1,
                            totalChunks = totalChunks,
                            liveLogs = currentLogs.toList()
                        )

                        equipmentRepo.batchInsertEquipment(chunk)

                        totalImported += chunk.size

                        addLog("Batch ${index + 1} saved successfully", LogType.SUCCESS)

                        val newProgress = (index + 1).toFloat() / totalChunks.toFloat()

                        _uiState.value = ImportingUiState.Importing(
                            progress = newProgress,
                            currentChunk = index + 1,
                            totalChunks = totalChunks,
                            liveLogs = currentLogs.toList()
                        )

                        delay(700)
                    } catch (e: Exception) {
                        addLog("Failed to save batch ${index + 1}: ${e.message}", LogType.ERROR)

                        totalFailed += chunk.size

                        val currentProgress = index.toFloat() / totalChunks.toFloat()

                        _uiState.value = ImportingUiState.Importing(
                            progress = currentProgress,
                            currentChunk = index + 1,
                            totalChunks = totalChunks,
                            liveLogs = currentLogs.toList()
                        )
                    }
                }

                cachedRawEquipment = emptyList()
                cachedUniqueDepartments = emptySet()

                addLog("All batches saved successfully", LogType.SUCCESS)

                val statsByDepartment = equipmentListToImport.groupBy { it.department.id }

                for ((deptId, equipmentInDepartment) in statsByDepartment) {
                    val department = departmentRepo.getDepartmentById(deptId) ?: continue
                    val newTotal = department.totalEquipment + equipmentInDepartment.size
                    val newDown = department.downEquipment!! + equipmentInDepartment.count { it.status == EquipmentStatus.DOWN }
                    val newDueService = department.dueServiceEquipment!! + equipmentInDepartment.count { it.status == EquipmentStatus.SERVICE }

                    departmentRepo.updateDepartment(
                        department.copy(
                            totalEquipment = newTotal,
                            downEquipment = newDown,
                            dueServiceEquipment = newDueService
                        )
                    )
                }

                _uiState.value = ImportingUiState.Success(
                    importedCount = totalImported,
                    skippedCount = skippedCount,
                    failedCount = totalFailed
                )

                _snackbarEvent.emit("${equipmentListToImport.size} Equipment imported successfully")
            } catch (e: Exception) {
                _uiState.value = ImportingUiState.Error(e.message ?: "Failed to import equipment")
                _snackbarEvent.emit(e.message ?: "Failed to parse Excel file")
            }
        }
    }

    fun resetState() {
        _uiState.value = ImportingUiState.Idle
    }

    fun selectFileForPreview(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val (fileName, fileSize) = getFileInfo(uri)

            val newItem = UploadedFile(uri, fileName, fileSize)
            _selectedFile.value = newItem

            val currentList = _uploadHistory.value.toMutableList()

            if (!currentList.any { it.fileName == fileName }) {
                currentList.add(0, newItem)
                _uploadHistory.value = currentList
            }

            parseExcelFile(uri)
        }
    }

    fun downloadTemplate() {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>()
            val fileName = "equipment_template.xlsx"
            val mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

            try {
                val exportTarget = FileExportHelper.getOutputStreamForExport(context, fileName, mimeType)

                exportTarget?.let { target ->
                    context.assets.open("equipment_importing_template.xlsx").use { inputStream ->
                        target.outputStream.use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }

                _snackbarEvent.emit("Template downloaded successfully")
            } catch (e: Exception) {
                _uiState.value = ImportingUiState.Error(e.message ?: "Failed to download template")
                _snackbarEvent.emit(e.message ?: "Failed to download template")
            }
        }
    }

    fun toggleRowSelection(rowId: String) {
        val previewState = _uiState.value as? ImportingUiState.PreviewReady ?: return

        val updatedRows = previewState.previewRows.map { row ->
            if (row.id == rowId && row.validationStatus == ValidationStatus.VALID) {
                row.copy(isSelected = !row.isSelected)
            } else {
                row
            }
        }

        val selectedRowsCount = updatedRows.count { it.isSelected }

        _uiState.value = previewState.copy(previewRows = updatedRows, selectedCount = selectedRowsCount)
    }

    fun toggleAllRowsSelection(selectAll: Boolean) {
        val previewState = _uiState.value as? ImportingUiState.PreviewReady ?: return

        val updatedRows = previewState.previewRows.map { row ->
            if (row.validationStatus == ValidationStatus.VALID) {
                row.copy(isSelected = selectAll)
            } else {
                row
            }
        }

        val selectedRowsCount = updatedRows.count { it.isSelected }

        _uiState.value = previewState.copy(previewRows = updatedRows, selectedCount = selectedRowsCount)
    }

    private fun getFileInfo(uri: Uri): Pair<String, Long> {
        var result: Pair<String, Long>? = null

        if (uri.scheme == "content") {
            val cursor: Cursor? = getApplication<Application>().contentResolver.query(uri, null, null, null)

            cursor.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                    if (nameIndex >= 0 && sizeIndex >= 0) {
                        result = cursor.getString(nameIndex) to cursor.getLong(sizeIndex)
                    }
                }
            }
        }

        return result ?: ("" to 0L)
    }
}



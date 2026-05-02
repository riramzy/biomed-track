package com.riramzy.biomedtrack.ui.screens.reports

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import com.riramzy.biomedtrack.domain.repo.PdfGeneratorRepo
import com.riramzy.biomedtrack.domain.repo.XlsxGeneratorRepo
import com.riramzy.biomedtrack.domain.usecase.reports.GenerateReportUseCase
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import javax.inject.Inject

data class GeneratedReport(
    val fileName: String,
    val fileSize: String,
    val isPdf: Boolean,
    val fileUri: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class ReportsUiState(
    val generatedReports: List<GeneratedReport> = emptyList(),
    val startDate: Long = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000,
    val departmentsList: List<Department> = emptyList(),
    val endDate: Long = System.currentTimeMillis(),
    val selectedDepartment: Department? = null,
    val selectedStatus: EquipmentStatus? = null,
    val includeLogs: Boolean = false,
    val exportFormat: String = "PDF",
    val totalCount: Int = 0,
    val healthyCount: Int = 0,
    val dueServiceCount: Int = 0,
    val downCount: Int = 0,
    val isFilterDialogOpen: Boolean = false,
    val generationLoading: Boolean = false,
    val generationResult: String? = null,
    val generationError: String? = null,
    val clearGenerationResult: Boolean = false
)

sealed class ReportsAction {
    data class SelectDateRange(val startDate: Long, val endDate: Long): ReportsAction()
    data class SelectDepartment(val department: Department?): ReportsAction()
    data class SelectStatus(val status: EquipmentStatus?): ReportsAction()
    data class ToggleLogs(val includeLogs: Boolean): ReportsAction()
    data class SelectFormat(val format: String): ReportsAction()
    data class SetFilterDialogVisibility(val visibility: Boolean): ReportsAction()
    data class ApplyCustomDateRange(val startDate: Long, val endDate: Long): ReportsAction()
    data class GeneratePdfReport(val outputStream: OutputStream?, val uri: String?): ReportsAction()
    data class GenerateExcelReport(val outputStream: OutputStream?, val uri: String?): ReportsAction()
    data object ClearGenerationResult: ReportsAction()
    data object Refresh: ReportsAction()
}

@HiltViewModel
class ReportsVm @Inject constructor(
    private val equipmentRepo: EquipmentRepo,
    private val departmentsRepo: DepartmentRepo,
    private val generateReportUseCase: GenerateReportUseCase,
    private val pdfGeneratorRepo: PdfGeneratorRepo,
    private val xlsxGeneratorRepo: XlsxGeneratorRepo
): ViewModel() {
    private val _uiState = MutableStateFlow(ReportsUiState(generationLoading = true))
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val departments = departmentsRepo.getAllDepartmentsOnce()
            _uiState.update { it.copy(departmentsList = departments) }
        }
        refreshSummary()
    }

    private fun refreshSummary() {
        viewModelScope.launch {
            _uiState.update { it.copy(generationLoading = true) }

            val allEquipment = equipmentRepo.getAllEquipmentOnce()

            val filteredEquipment = allEquipment.filter { equipment ->
                val matchesDepartment = _uiState.value.selectedDepartment == null || equipment.department == _uiState.value.selectedDepartment
                val matchesStatus = _uiState.value.selectedStatus == null || equipment.status == _uiState.value.selectedStatus
                matchesDepartment && matchesStatus
            }

            _uiState.update { state ->
                state.copy(
                    totalCount = filteredEquipment.size,
                    healthyCount = filteredEquipment.count { it.status == EquipmentStatus.ONLINE },
                    dueServiceCount = filteredEquipment.count { it.status == EquipmentStatus.SERVICE },
                    downCount = filteredEquipment.count { it.status == EquipmentStatus.DOWN },
                    generationLoading = false
                )
            }
        }
    }

    fun onAction(action: ReportsAction) {
        when(action) {
            is ReportsAction.SelectDateRange -> {
                _uiState.update { it.copy(startDate = action.startDate, endDate = action.endDate) }
                refreshSummary()
            }
            is ReportsAction.SelectDepartment -> {
                _uiState.update { it.copy(selectedDepartment = action.department) }
                refreshSummary()
            }
            is ReportsAction.SelectStatus -> {
                _uiState.update { it.copy(selectedStatus = action.status) }
                refreshSummary()
            }
            is ReportsAction.ToggleLogs -> {
                _uiState.update { it.copy(includeLogs = action.includeLogs) }
                refreshSummary()
            }
            is ReportsAction.SelectFormat -> {
                _uiState.update { it.copy(exportFormat = action.format) }
            }
            is ReportsAction.SetFilterDialogVisibility -> {
                _uiState.update { it.copy(isFilterDialogOpen = action.visibility) }
            }
            is ReportsAction.ApplyCustomDateRange -> {
                _uiState.update { it.copy(startDate = action.startDate, endDate = action.endDate) }
                refreshSummary()
            }
            is ReportsAction.Refresh -> {
                refreshSummary()
            }
            is ReportsAction.ClearGenerationResult -> {
                _uiState.update { it.copy(generationResult = null, generationError = null) }
            }
            is ReportsAction.GeneratePdfReport -> {
                if (action.outputStream == null || action.uri == null) {
                    _uiState.update { it.copy(generationError = "Failed to create target file", generationLoading = false) }
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    _uiState.update { it.copy(generationLoading = true) }

                    val result = generateReportUseCase(
                        startDate = _uiState.value.startDate,
                        endDate = _uiState.value.endDate,
                        department = _uiState.value.selectedDepartment,
                        includeHealthy = _uiState.value.selectedStatus == EquipmentStatus.ONLINE || _uiState.value.selectedStatus == null,
                        includeDown = _uiState.value.selectedStatus == EquipmentStatus.DOWN || _uiState.value.selectedStatus == null,
                        includeServiceDue = _uiState.value.selectedStatus == EquipmentStatus.SERVICE || _uiState.value.selectedStatus == null
                    )

                    when(result) {
                        is Result.Success -> {
                            val memoryOutputStream = ByteArrayOutputStream()

                            val pdfResult = pdfGeneratorRepo.generatePdfReport(
                                reportData = result.data,
                                outputStream = memoryOutputStream,
                                withLogs = _uiState.value.includeLogs,
                            )

                            when(pdfResult) {
                                is Result.Success -> {
                                    val pdfBytes = memoryOutputStream.toByteArray()
                                    val sizeStream = formatFileSize(pdfBytes.size.toLong())

                                    action.outputStream.use { it.write(pdfBytes) }

                                    val newReport = GeneratedReport(
                                        fileName = "BioMedTrack_Report_${System.currentTimeMillis()}.pdf",
                                        fileSize = sizeStream,
                                        fileUri = action.uri,
                                        isPdf = true
                                    )
                                    _uiState.update {
                                        it.copy(
                                            generationResult = "Report generated successfully",
                                            generationLoading = false,
                                            generatedReports = it.generatedReports.plus(newReport)
                                        )
                                    }

                                }
                                is Result.Error -> {
                                    _uiState.update { it.copy(generationError = pdfResult.message, generationLoading = false) }
                                }
                                else -> {
                                    _uiState.update { it.copy(generationError = "Unknown error", generationLoading = false) }
                                }
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(generationError = result.message, generationLoading = false) }
                        }
                        else -> {
                            _uiState.update { it.copy(generationError = "Unknown error", generationLoading = false) }
                        }
                    }
                }
            }
            is ReportsAction.GenerateExcelReport -> {
                if (action.outputStream == null  || action.uri == null) {
                    _uiState.update { it.copy(generationError = "Failed to create target file", generationLoading = false) }
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    _uiState.update { it.copy(generationLoading = true) }

                    val result = generateReportUseCase(
                        startDate = _uiState.value.startDate,
                        endDate = _uiState.value.endDate,
                        department = _uiState.value.selectedDepartment,
                        includeHealthy = _uiState.value.selectedStatus == EquipmentStatus.ONLINE || _uiState.value.selectedStatus == null,
                        includeDown = _uiState.value.selectedStatus == EquipmentStatus.DOWN || _uiState.value.selectedStatus == null,
                        includeServiceDue = _uiState.value.selectedStatus == EquipmentStatus.SERVICE || _uiState.value.selectedStatus == null
                    )

                    when(result) {
                        is Result.Success -> {
                            val memoryOutputStream = ByteArrayOutputStream()

                            val excelResult = xlsxGeneratorRepo.generateExcelReport(
                                reportData = result.data,
                                outputStream = memoryOutputStream,
                                withLogs = _uiState.value.includeLogs,
                            )

                            when(excelResult) {
                                is Result.Success -> {
                                    val excelBytes = memoryOutputStream.toByteArray()
                                    val sizeStream = formatFileSize(excelBytes.size.toLong())

                                    action.outputStream.use { it.write(excelBytes) }

                                    val generatedReport = GeneratedReport(
                                        fileName = "BioMedTrack_Report_${System.currentTimeMillis()}.xlsx",
                                        fileSize = sizeStream,
                                        fileUri = action.uri,
                                        isPdf = false
                                    )

                                    _uiState.update {
                                        it.copy(
                                            generationResult = "Report generated successfully",
                                            generationLoading = false,
                                            generatedReports = it.generatedReports.plus(generatedReport)
                                        )
                                    }
                                }
                                is Result.Error -> {
                                    _uiState.update { it.copy(generationError = excelResult.message, generationLoading = false) }
                                }
                                else -> {
                                    _uiState.update { it.copy(generationError = "Unknown error", generationLoading = false) }
                                }
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(generationError = result.message, generationLoading = false) }
                        }
                        else -> {
                            _uiState.update { it.copy(generationError = "Unknown error", generationLoading = false) }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatFileSize(sizeInBytes: Long): String {
        val sizeInMb = sizeInBytes / (1024.0 * 1024.0)
        return if (sizeInMb >= 1.0) {
            String.format("%.1f MB", sizeInMb)
        } else {
            val sizeInKb = sizeInBytes / 1024.0
            String.format("%.1f KB", sizeInKb)
        }
    }
}
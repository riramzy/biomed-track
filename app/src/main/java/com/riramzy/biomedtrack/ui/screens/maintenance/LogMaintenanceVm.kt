package com.riramzy.biomedtrack.ui.screens.maintenance

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.repo.StorageRepo
import com.riramzy.biomedtrack.domain.usecase.equipment.GetEquipmentByIdUseCase
import com.riramzy.biomedtrack.domain.usecase.maintenance.AddMaintenanceLogUseCase
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.MaintenanceType
import com.riramzy.biomedtrack.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class LogMaintenanceUiState(
    val equipmentName: String = "",
    val equipmentSerial: String = "",
    val equipmentModel: String = "",
    val department: Department = Department("", "", 25),
    val type: MaintenanceType = MaintenanceType.REPAIR,
    val currentStatus: EquipmentStatus = EquipmentStatus.SERVICE,
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val workDone: String = "",
    val technicianName: String = "",
    val checklist: List<ChecklistItem> = emptyList(),
    val capturedPhotoUri: String? = null,
    val isUploading: Boolean = false,
    val isLoading: Boolean = false,
    val isError: String? = null,
    val isSuccess: Boolean = false
)

sealed class LogMaintenanceAction {
    data class UpdateType(val type: MaintenanceType): LogMaintenanceAction()
    data class UpdateStatus(val status: EquipmentStatus): LogMaintenanceAction()
    data class ToggleChecklistItem(val item: ChecklistItem): LogMaintenanceAction()
    data class UpdateNotes(val notes: String): LogMaintenanceAction()
    data class UpdateDate(val date: Long): LogMaintenanceAction()
    data class AddPhoto(val uri: String): LogMaintenanceAction()
    data object ResetError: LogMaintenanceAction()
    data object Save: LogMaintenanceAction()
}

@HiltViewModel
class LogMaintenanceVm @Inject constructor(
    stateHandle: SavedStateHandle,
    private val getEquipmentByIdUseCase: GetEquipmentByIdUseCase,
    private val addMaintenanceLogUseCase: AddMaintenanceLogUseCase,
    private val storageRepo: StorageRepo,
    sessionManager: SessionManager
): ViewModel() {
    private val _uiState = MutableStateFlow(LogMaintenanceUiState())
    val uiState: StateFlow<LogMaintenanceUiState> = _uiState.asStateFlow()
    private val equipmentId = stateHandle.get<String>("equipmentId") ?: ""
    private val user = sessionManager.currentUser.value

    init {
        initializeChecklist()
        loadEquipmentData()
    }

    private fun loadEquipmentData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val equipment = getEquipmentByIdUseCase(equipmentId)

            if (user == null) {
                _uiState.update { it.copy(isError = "Session expired", isLoading = false) }
                return@launch
            }

            if (equipment == null) {
                _uiState.update { it.copy(isError = "Equipment not found", isLoading = false) }
                return@launch
            }

            if (equipmentId.isNotBlank()) {
                _uiState.update {
                    it.copy(
                        equipmentName = equipment.name,
                        equipmentSerial = equipment.serialNumber,
                        department = equipment.department,
                        type = it.type,
                        currentStatus = equipment.status,
                        date = it.date,
                        notes = it.notes,
                        workDone = it.workDone,
                        technicianName = user.name,
                        checklist = it.checklist,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun initializeChecklist() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    checklist = listOf(
                        ChecklistItem("1", "Visual Inspection", false),
                        ChecklistItem("2", "Electrical Safety Check", false),
                        ChecklistItem("3", "Cleaning", false),
                        ChecklistItem("4", "Functional Test", false),
                        ChecklistItem("5", "Alarm Verification,", false),
                        ChecklistItem("6", "Filters Inspection", false),
                        ChecklistItem("7", "Calibration", false),
                        ChecklistItem("8", "Wear Parts Evaluation", false),
                        ChecklistItem("9", "Software Check", false),
                        ChecklistItem("10", "Documentation", false)
                    )
                )
            }
        }
    }

    private fun toggleChecklistItem(item: ChecklistItem) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedItems = currentState.checklist.map { existingItem ->
                    if (existingItem.id == item.id) {
                        existingItem.copy(isChecked = !existingItem.isChecked)
                    } else {
                        existingItem
                    }
                }
                currentState.copy(checklist = updatedItems)
            }
        }
    }

    private fun saveMaintenanceLog() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentState = _uiState.value
            val logId = UUID.randomUUID().toString()
            var finalPhotoUrl: String? = null

            if (user == null) {
                _uiState.update { it.copy(isError = "Session expired", isLoading = false) }
                return@launch
            }

            if (currentState.capturedPhotoUri != null) {
                val uploadResult = storageRepo.uploadFile(
                    path = "maintenance_logs/$logId",
                    uri = currentState.capturedPhotoUri.toUri()
                )

                if (uploadResult is Result.Success) {
                    finalPhotoUrl = uploadResult.data
                } else if (uploadResult is Result.Error) {
                    _uiState.update { it.copy(isError = uploadResult.message, isLoading = false) }
                    return@launch
                }
            }

            val log = MaintenanceLog(
                id = logId,
                equipmentId = equipmentId,
                equipmentName = currentState.equipmentName,
                equipmentModel = currentState.equipmentModel,
                equipmentSerial = currentState.equipmentSerial,
                department = currentState.department,
                type = currentState.type,
                technicianId = user.id,
                technicianName = user.name,
                date = currentState.date,
                currentStatus = currentState.currentStatus,
                checklist = currentState.checklist,
                notes = currentState.notes,
                workDone = currentState.workDone,
                photoUrl = finalPhotoUrl
            )

            when(val result = addMaintenanceLogUseCase(log)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isSuccess = true, isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isError = result.message, isLoading = false) }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true, isSuccess = false) }
                }
            }
        }
    }

    fun onAction(action: LogMaintenanceAction) {
        when(action) {
            is LogMaintenanceAction.UpdateType -> { _uiState.update { it.copy(type = action.type) } }
            is LogMaintenanceAction.UpdateStatus -> { _uiState.update { it.copy(currentStatus = action.status) } }
            is LogMaintenanceAction.UpdateDate -> { _uiState.update { it.copy(date = action.date) } }
            is LogMaintenanceAction.ToggleChecklistItem -> { toggleChecklistItem(action.item) }
            is LogMaintenanceAction.UpdateNotes -> { _uiState.update { it.copy(notes = action.notes) } }
            is LogMaintenanceAction.AddPhoto -> { _uiState.update { it.copy(capturedPhotoUri = action.uri) } }
            is LogMaintenanceAction.ResetError -> { _uiState.update { it.copy(isError = null) } }
            is LogMaintenanceAction.Save -> { saveMaintenanceLog() }
        }
    }
}
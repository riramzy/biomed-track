package com.riramzy.biomedtrack.ui.screens.equipment.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.usecase.equipment.ChangeEquipmentStatusUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.DeleteEquipmentUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.GetAllEquipmentUseCase
import com.riramzy.biomedtrack.domain.usecase.maintenance.GetLogsForEquipmentUseCase
import com.riramzy.biomedtrack.domain.usecase.statuschange.GetStatusChangesUseCase
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EquipmentDetailsUiState {
    data object Loading: EquipmentDetailsUiState()
    data class Success(
        val currentUser: Technician,
        val equipment: Equipment,
        val maintenanceLogs: List<MaintenanceLog>,
        val statusChangesLogs: List<StatusChangeLog>,
        val canEditEquipment: Boolean,
        val canDeleteEquipment: Boolean,
        val canChangeStatus: Boolean
    ): EquipmentDetailsUiState()
    data class Error(val message: String): EquipmentDetailsUiState()
}

@HiltViewModel
class EquipmentDetailsVm @Inject constructor(
    stateHandler: SavedStateHandle,
    private val getAllEquipmentUseCase: GetAllEquipmentUseCase,
    private val getLogsForEquipmentUseCase: GetLogsForEquipmentUseCase,
    private val getStatusChangesUseCase: GetStatusChangesUseCase,
    private val changeEquipmentStatusUseCase: ChangeEquipmentStatusUseCase,
    private val deleteEquipmentUseCase: DeleteEquipmentUseCase,
    private val sessionManager: SessionManager
): ViewModel() {
    private val _uiState = MutableStateFlow<EquipmentDetailsUiState>(EquipmentDetailsUiState.Loading)
    val uiState: StateFlow<EquipmentDetailsUiState> = _uiState.asStateFlow()
    private val equipmentId = stateHandler.get<String>("equipmentId") ?: ""
    private val _deleteResult = MutableStateFlow<Result<Unit>?>(null)
    val deleteResult: StateFlow<Result<Unit>?> = _deleteResult.asStateFlow()

    init {
        _uiState.value = EquipmentDetailsUiState.Loading

        viewModelScope.launch {
            val currentUserFlow = sessionManager.currentUser

            val equipmentFlow = getAllEquipmentUseCase().map { equipments ->
                equipments.find { it.id == equipmentId }
            }

            combine(
                flow = equipmentFlow,
                flow2 = getLogsForEquipmentUseCase(equipmentId),
                flow3 = getStatusChangesUseCase(equipmentId),
                flow4 = currentUserFlow
            ) { equipment, logs, statusChanges, currentUser ->
                if (currentUser == null) {
                    EquipmentDetailsUiState.Error(message = "Cannot find current user")
                } else if (equipment == null) {
                    EquipmentDetailsUiState.Error(message = "Cannot find equipment")
                } else {
                    EquipmentDetailsUiState.Success(
                        currentUser = currentUser,
                        equipment = equipment,
                        maintenanceLogs = logs,
                        statusChangesLogs = statusChanges,
                        canEditEquipment = sessionManager.hasPermission(Permission.EDIT_EQUIPMENT),
                        canDeleteEquipment = sessionManager.hasPermission(Permission.DELETE_EQUIPMENT),
                        canChangeStatus = sessionManager.hasPermission(Permission.EDIT_EQUIPMENT)
                    )
                }
            }.catch { e ->
                EquipmentDetailsUiState.Error(message = e.message ?: "Failed to connect to database")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun deleteEquipment() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteEquipmentUseCase(equipmentId)
            _deleteResult.value = result
        }
    }

    fun changeEquipmentStatus(equipment: Equipment, newStatus: EquipmentStatus, note: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            changeEquipmentStatusUseCase(equipment, newStatus, note)
        }
    }

    fun resetDeleteResult() {
        _deleteResult.value = null
    }
}
package com.riramzy.biomedtrack.ui.screens.equipment.edit

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.repo.StorageRepo
import com.riramzy.biomedtrack.domain.usecase.department.GetAllDepartmentsUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.GetEquipmentByIdUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.UpdateEquipmentUseCase
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditEquipmentUiState(
    val id: String = "",
    val name: String = "",
    val model: String = "",
    val serialNumber: String = "",
    val manufacturer: String = "",
    val agent: String = "",
    val category: String = "",
    val location: String = "",
    val department: Department? = null,
    val departments: List<Department> = emptyList(),
    val currentStatus: EquipmentStatus? = null,
    val installDate: Long = 0L,
    val contractInfo: String? = "",
    val warrantyEndDate: Long? = null,
    val capturedPhotoUri: String? = null,
    val isLoading: Boolean = false,
    val isError: String? = null,
    val isSuccess: Boolean = false
)

sealed class EditEquipmentAction {
    data class UpdateId(val id: String): EditEquipmentAction()
    data class UpdateName(val name: String): EditEquipmentAction()
    data class UpdateModel(val model: String): EditEquipmentAction()
    data class UpdateSerialNumber(val serialNumber: String): EditEquipmentAction()
    data class UpdateManufacturer(val manufacturer: String): EditEquipmentAction()
    data class UpdateAgent(val agent: String): EditEquipmentAction()
    data class UpdateCategory(val category: String): EditEquipmentAction()
    data class UpdateLocation(val location: String): EditEquipmentAction()
    data class UpdateDepartment(val department: Department): EditEquipmentAction()
    data class UpdateCurrentStatus(val currentStatus: EquipmentStatus): EditEquipmentAction()
    data class UpdateInstallDate(val installDate: Long): EditEquipmentAction()
    data class UpdateContractInfo(val contractInfo: String): EditEquipmentAction()
    data class UpdateWarrantyEndDate(val warrantyEndDate: Long): EditEquipmentAction()
    data class AddPhoto(val uri: String) : EditEquipmentAction()
    data object ResetError: EditEquipmentAction()
    data object Save: EditEquipmentAction()
}

@HiltViewModel
class EditEquipmentVm @Inject constructor(
    @param:ApplicationContext val context: Context,
    stateHandle: SavedStateHandle,
    private val sessionManager: SessionManager,
    private val getEquipmentByIdUseCase: GetEquipmentByIdUseCase,
    private val getAllDepartmentsUseCase: GetAllDepartmentsUseCase,
    private val storageRepo: StorageRepo,
    private val updateEquipmentUseCase: UpdateEquipmentUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(EditEquipmentUiState())
    val uiState: StateFlow<EditEquipmentUiState> = _uiState.asStateFlow()
    private val equipmentId: String = stateHandle.get<String>("equipmentId") ?: ""
    val currentUser = sessionManager.currentUser

    init {
        loadEquipmentData()
        fetchDepartments()
    }

    private fun fetchDepartments() {
        viewModelScope.launch {
            val departments = getAllDepartmentsUseCase().first()
            _uiState.update {
                it.copy(departments = departments)
            }
        }
    }

    private fun loadEquipmentData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val equipment = getEquipmentByIdUseCase(equipmentId)
            if (equipment == null) {
                _uiState.update {
                    it.copy(
                        isError = context.getString(R.string.no_equipment_found),
                        isLoading = false
                    )
                }
                return@launch
            }

            if (equipmentId.isNotBlank()) {
                _uiState.update {
                    it.copy(
                        id = equipment.id,
                        name = equipment.name,
                        model = equipment.model,
                        serialNumber = equipment.serialNumber,
                        manufacturer = equipment.manufacturer,
                        agent = equipment.agent,
                        category = equipment.category,
                        location = equipment.location,
                        department = equipment.department,
                        currentStatus = equipment.status,
                        installDate = equipment.installDate,
                        warrantyEndDate = equipment.warrantyEndDate ?: 0L,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isError = context.getString(R.string.no_equipment_found),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateEquipment() {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.update { it.copy(isLoading = true) }

            var finalPhotoUrl: String? = null

            if (currentState.capturedPhotoUri != null) {
                val uploadResult = storageRepo.uploadFile(
                    path = "new_equipment/${currentState.name} ${currentState.model} ${currentState.serialNumber}",
                    uri = currentState.capturedPhotoUri.toUri()
                )

                if (uploadResult is Result.Success) {
                    finalPhotoUrl = uploadResult.data
                } else if (uploadResult is Result.Error) {
                    _uiState.update { it.copy(isError = uploadResult.message, isLoading = false) }
                    return@launch
                }
            }

            if (currentState.department == null) {
                _uiState.update {
                    it.copy(
                        isError = context.getString(R.string.error_select_department),
                        isLoading = false
                    )
                }
                return@launch
            }

            if (currentState.currentStatus == null) {
                _uiState.update {
                    it.copy(
                        isError = context.getString(R.string.error_select_status),
                        isLoading = false
                    )
                }
                return@launch
            }

            if (sessionManager.currentUser.value == null) {
                _uiState.update {
                    it.copy(
                        isError = context.getString(R.string.error_session_expired),
                        isLoading = false
                    )
                }
                return@launch
            }

            val updatedEquipment = Equipment(
                id = currentState.id,
                name = currentState.name,
                model = currentState.model,
                serialNumber = currentState.serialNumber,
                manufacturer = currentState.manufacturer,
                agent = currentState.agent,
                category = currentState.category,
                location = currentState.location,
                department = currentState.department,
                status = currentState.currentStatus,
                installDate = currentState.installDate,
                warrantyEndDate = currentState.warrantyEndDate,
                createdBy = sessionManager.currentUser.value!!.name,
                serviceIntervalDays = 30,
                contractInfo = currentState.contractInfo,
            )

            when(val result = updateEquipmentUseCase(updatedEquipment)) {
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

    fun onAction(action: EditEquipmentAction) {
        when(action) {
            is EditEquipmentAction.UpdateId -> { _uiState.update { it.copy(id = action.id) } }
            is EditEquipmentAction.UpdateName -> { _uiState.update { it.copy(name = action.name) } }
            is EditEquipmentAction.UpdateModel -> { _uiState.update { it.copy(model = action.model) } }
            is EditEquipmentAction.UpdateSerialNumber -> { _uiState.update { it.copy(serialNumber = action.serialNumber) } }
            is EditEquipmentAction.UpdateManufacturer -> { _uiState.update { it.copy(manufacturer = action.manufacturer) } }
            is EditEquipmentAction.UpdateAgent -> { _uiState.update { it.copy(agent = action.agent) } }
            is EditEquipmentAction.UpdateCategory -> { _uiState.update { it.copy(category = action.category) } }
            is EditEquipmentAction.UpdateLocation -> { _uiState.update { it.copy(location = action.location) } }
            is EditEquipmentAction.UpdateDepartment -> { _uiState.update { it.copy(department = action.department) } }
            is EditEquipmentAction.UpdateCurrentStatus -> { _uiState.update { it.copy(currentStatus = action.currentStatus) } }
            is EditEquipmentAction.UpdateInstallDate -> { _uiState.update { it.copy(installDate = action.installDate) } }
            is EditEquipmentAction.UpdateContractInfo -> { _uiState.update { it.copy(contractInfo = action.contractInfo) } }
            is EditEquipmentAction.UpdateWarrantyEndDate -> { _uiState.update { it.copy(warrantyEndDate = action.warrantyEndDate) } }
            is EditEquipmentAction.AddPhoto -> {
                _uiState.update { it.copy(capturedPhotoUri = action.uri) }
            }
            is EditEquipmentAction.ResetError -> { _uiState.update { it.copy(isError = null) } }
            is EditEquipmentAction.Save -> {
                updateEquipment()

            }
        }
    }
}
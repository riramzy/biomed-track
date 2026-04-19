package com.riramzy.biomedtrack.ui.screens.equipment.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.usecase.department.GetAllDepartmentsUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.AddEquipmentUseCase
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEquipmentUiState(
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
    val installDate: String = "",
    val warrantyEndDate: String? = "",
    val isLoading: Boolean = false,
    val isError: String? = null,
    val isSuccess: Boolean = false
)

sealed class AddEquipmentAction {
    data class UpdateName(val name: String): AddEquipmentAction()
    data class UpdateModel(val model: String): AddEquipmentAction()
    data class UpdateSerialNumber(val serialNumber: String): AddEquipmentAction()
    data class UpdateManufacturer(val manufacturer: String): AddEquipmentAction()
    data class UpdateAgent(val agent: String): AddEquipmentAction()
    data class UpdateCategory(val category: String): AddEquipmentAction()
    data class UpdateLocation(val location: String): AddEquipmentAction()
    data class UpdateDepartment(val department: Department): AddEquipmentAction()
    data class UpdateCurrentStatus(val currentStatus: EquipmentStatus): AddEquipmentAction()
    data class UpdateInstallDate(val installDate: String): AddEquipmentAction()
    data class UpdateWarrantyEndDate(val warrantyEndDate: String): AddEquipmentAction()
    data object ResetError: AddEquipmentAction()
    data object Save: AddEquipmentAction()
}

@HiltViewModel
class AddEquipmentVm @Inject constructor(
    private val addEquipmentUseCase: AddEquipmentUseCase,
    private val getAllDepartmentsUseCase: GetAllDepartmentsUseCase,
    private val sessionManager: SessionManager
): ViewModel() {
    private val _uiState = MutableStateFlow(AddEquipmentUiState())
    val uiState: StateFlow<AddEquipmentUiState> = _uiState.asStateFlow()

    init {
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

    fun onAction(action: AddEquipmentAction) {
        when(action) {
            is AddEquipmentAction.UpdateName -> { _uiState.update { it.copy(name = action.name) } }
            is AddEquipmentAction.UpdateModel -> { _uiState.update { it.copy(model = action.model) } }
            is AddEquipmentAction.UpdateSerialNumber -> { _uiState.update { it.copy(serialNumber = action.serialNumber) } }
            is AddEquipmentAction.UpdateManufacturer -> { _uiState.update { it.copy(manufacturer = action.manufacturer) } }
            is AddEquipmentAction.UpdateAgent -> { _uiState.update { it.copy(agent = action.agent) } }
            is AddEquipmentAction.UpdateCategory -> { _uiState.update { it.copy(category = action.category) } }
            is AddEquipmentAction.UpdateLocation -> { _uiState.update { it.copy(location = action.location) } }
            is AddEquipmentAction.UpdateDepartment -> { _uiState.update { it.copy(department = action.department) } }
            is AddEquipmentAction.UpdateCurrentStatus -> { _uiState.update { it.copy(currentStatus = action.currentStatus) } }
            is AddEquipmentAction.UpdateInstallDate -> { _uiState.update { it.copy(installDate = action.installDate) } }
            is AddEquipmentAction.UpdateWarrantyEndDate -> { _uiState.update { it.copy(warrantyEndDate = action.warrantyEndDate) } }
            is AddEquipmentAction.ResetError -> { _uiState.update { it.copy(isError = null) } }
            is AddEquipmentAction.Save -> { saveEquipment() }
        }
    }

    private fun saveEquipment() {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.update { it.copy(isLoading = true) }

            when(currentState) {
                is AddEquipmentUiState -> {
                    if (currentState.name.isEmpty()) {
                        _uiState.update { it.copy(isError = "Name cannot be empty", isLoading = false) }
                        return@launch
                    }
                    if (currentState.model.isEmpty()) {
                        _uiState.update { it.copy(isError = "Model cannot be empty", isLoading = false) }
                        return@launch
                    }
                    if (currentState.serialNumber.isEmpty()) {
                        _uiState.update { it.copy(isError = "Serial Number cannot be empty", isLoading = false) }
                        return@launch
                    }
                    if (currentState.manufacturer.isEmpty()) {
                        _uiState.update { it.copy(isError = "Manufacturer cannot be empty", isLoading = false) }
                        return@launch
                    }
                    if (currentState.agent.isEmpty()) {
                        _uiState.update { it.copy(isError = "Agent cannot be empty", isLoading = false) }
                        return@launch
                    }
                    if (currentState.category.isEmpty()) {
                        _uiState.update { it.copy(isError = "Please select a category", isLoading = false) }
                        return@launch
                    }
                    if (currentState.currentStatus == null) {
                        _uiState.update { it.copy(isError = "Please select a status", isLoading = false) }
                        return@launch
                    }
                    if (currentState.location.isEmpty()) {
                        _uiState.update { it.copy(isError = "Please select a location", isLoading = false) }
                        return@launch
                    }
                    if (currentState.installDate.isEmpty()) {
                        _uiState.update { it.copy(isError = "Please select an install date", isLoading = false) }
                        return@launch
                    }
                    if (currentState.warrantyEndDate == null) {
                        _uiState.update { it.copy(isError = "Please select a warranty end date", isLoading = false) }
                        return@launch
                    }
                }
            }

            if (currentState.department == null) {
                _uiState.update { it.copy(isError = "Please select a department", isLoading = false) }
                return@launch
            }

            if (sessionManager.currentUser.value == null) {
                _uiState.update { it.copy(isError = "Session expired", isLoading = false) }
                return@launch
            }

            val equipment = Equipment(
                id = "",
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
                serviceIntervalDays = 30,
                createdBy = sessionManager.currentUser.value!!.name
            )

            when(val result = addEquipmentUseCase(equipment)) {
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
}
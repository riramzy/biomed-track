package com.riramzy.biomedtrack.ui.screens.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import com.riramzy.biomedtrack.domain.usecase.equipment.ChangeEquipmentStatusUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.GetAllEquipmentUseCase
import com.riramzy.biomedtrack.utils.EquipmentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class InventoryUiState {
    data object Loading: InventoryUiState()
    data class Success(
        val equipment: List<Equipment>,
        val departments: List<Department>,
        val selectedDepartment: Department?,
        val searchQuery: String,
        val canAddEquipment: Boolean,
        val canDeleteEquipment: Boolean,
    ): InventoryUiState()
    data class Error(val message: String): InventoryUiState()
}

@HiltViewModel
class InventoryVm @Inject constructor(
    private val getAllEquipmentsUseCase: GetAllEquipmentUseCase,
    private val changeEquipmentStatusUseCase: ChangeEquipmentStatusUseCase,
    private val departmentRepo: DepartmentRepo,
    private val sessionManager: SessionManager
): ViewModel() {
    private val _uiState = MutableStateFlow<InventoryUiState>(InventoryUiState.Loading)
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    private val _selectedDepartment = MutableStateFlow<Department?>(null)

    init {
        _uiState .value = InventoryUiState.Loading
        viewModelScope.launch {
            combine(
                getAllEquipmentsUseCase(),
                _searchQuery,
                _selectedDepartment,
                departmentRepo.getAllDepartments()
            ) { equipment, query, selectedDepartment, departments ->
                val filteredByDepartment = if (selectedDepartment != null) {
                    equipment.filter { it.department == selectedDepartment }
                } else {
                    equipment
                }

                val filteredByQuery = if (query.isEmpty()) {
                    filteredByDepartment
                } else {
                    filteredByDepartment.filter {
                        it.name.contains(query, ignoreCase = true) ||
                                it.model.contains(query, ignoreCase = true) ||
                                it.serialNumber.contains(query, ignoreCase = true)
                    }
                }

                InventoryUiState.Success(
                    equipment = filteredByQuery,
                    departments = departments,
                    selectedDepartment = selectedDepartment,
                    searchQuery = query,
                    canAddEquipment = sessionManager.hasPermission(Permission.ADD_EQUIPMENT),
                    canDeleteEquipment = sessionManager.hasPermission(Permission.DELETE_EQUIPMENT)
                )
            }.catch { e ->
                _uiState.value = InventoryUiState.Error(e.message ?: "Failed to connect to database")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun searchInventory(query: String) {
        _searchQuery.value = query
    }

    fun filterByDepartment(department: Department?) {
        _selectedDepartment.value = department
    }

    fun changeStatus(equipment: Equipment, newStatus: EquipmentStatus, notes: String) {
        viewModelScope.launch {
            changeEquipmentStatusUseCase(equipment, newStatus, notes)
        }
    }
}
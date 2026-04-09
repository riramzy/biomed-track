package com.riramzy.biomedtrack.ui.screens.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.usecase.equipment.GetAllEquipmentUseCase
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
    private val sessionManager: SessionManager
): ViewModel() {
    private val _uiState = MutableStateFlow<InventoryUiState>(InventoryUiState.Loading)
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    private var _searchQuery = MutableStateFlow("")
    private var _selectedDepartment = MutableStateFlow<Department?>(null)

    init {
        _uiState .value = InventoryUiState.Loading

        viewModelScope.launch {
            combine(
                getAllEquipmentsUseCase(),
                _searchQuery,
                _selectedDepartment,
                sessionManager.currentUser
            ) { equipment, query, department, user ->
                val filteredByDepartment = if (department != null) {
                    equipment.filter { it.department == department }
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

                val hasPermission = user?.assignedDepartments?.contains(department) ?: false

                InventoryUiState.Success(
                    equipment = filteredByQuery,
                    selectedDepartment = department,
                    searchQuery = query,
                    canAddEquipment = hasPermission,
                    canDeleteEquipment = hasPermission
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

    fun filterByDepartment(department: Department) {
        _selectedDepartment.value = department
    }
}
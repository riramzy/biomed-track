package com.riramzy.biomedtrack.ui.screens.inventory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import com.riramzy.biomedtrack.domain.usecase.equipment.ChangeEquipmentStatusUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.GetAllEquipmentUseCase
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
        val currentUser: Technician,
        val equipment: List<Equipment>,
        val departments: List<Department>,
        val categories: List<String>,
        val selectedDepartment: Department?,
        val searchQuery: String,
        val selectedCategory: String?,
        val selectedStatus: EquipmentStatus?,
        val canAddEquipment: Boolean,
        val canDeleteEquipment: Boolean,
    ): InventoryUiState()
    data class Error(val message: String): InventoryUiState()
}

data class InventoryFilters(
    val department: Department? = null,
    val category: String? = null,
    val status: EquipmentStatus? = null,
    val query: String = ""
)

@HiltViewModel
class InventoryVm @Inject constructor(
    private val getAllEquipmentsUseCase: GetAllEquipmentUseCase,
    private val changeEquipmentStatusUseCase: ChangeEquipmentStatusUseCase,
    private val departmentRepo: DepartmentRepo,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow<InventoryUiState>(InventoryUiState.Loading)
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()
    private val _filters = MutableStateFlow(InventoryFilters())
    private var fetchJob: Job? = null

    init {
        val initialDepartmentName = savedStateHandle.get<String>("department")

        viewModelScope.launch {
            if (initialDepartmentName != null) {
                val departments = departmentRepo.getAllDepartmentsOnce()
                val targetDept =
                    departments.find { it.name.equals(initialDepartmentName, ignoreCase = true) }

                if (targetDept != null) {
                    _filters.value = _filters.value.copy(department = targetDept)
                }
            }
        }
        loadInventory()
    }

    private fun loadInventory() {
        fetchJob?.cancel()
        _uiState .value = InventoryUiState.Loading

        viewModelScope.launch {
            combine(
                getAllEquipmentsUseCase(),
                _filters,
                departmentRepo.getAllDepartments()
            ) { equipment, filters, departments ->
                val currentUser = sessionManager.currentUser.value

                if (currentUser == null) {
                    _uiState.value = InventoryUiState.Error("Failed to get current user")
                    return@combine InventoryUiState.Error("Failed to get current user")
                }

                val accessibleEquipment = if (currentUser.role == UserRole.TECHNICIAN) {
                    val assignedNames = currentUser.assignedDepartments.map { it.name }.toSet()
                    val assignedIds = currentUser.assignedDepartments.map { it.id }.toSet()

                    equipment.filter { it.department.id in assignedIds || it.department.name in assignedNames }
                } else {
                    equipment
                }

                val accessibleDepartments = if (currentUser.role == UserRole.TECHNICIAN) {
                    currentUser.assignedDepartments
                } else {
                    departments
                }

                val allCategories = accessibleEquipment.map { it.category }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()

                val filteredByDepartment = if (filters.department != null) {
                    accessibleEquipment.filter { it.department.id == filters.department.id }
                } else {
                    accessibleEquipment
                }

                val filteredByStatus = if (filters.status != null) {
                    filteredByDepartment.filter { it.status == filters.status }
                } else {
                    filteredByDepartment
                }

                val filteredByCategory = if (filters.category != null) {
                    filteredByStatus.filter { it.category == filters.category }
                } else {
                    filteredByStatus
                }

                val finalFiltered = if (filters.query.isEmpty()) {
                    filteredByCategory
                } else {
                    filteredByCategory.filter {
                        it.name.contains(filters.query, ignoreCase = true) ||
                                it.model.contains(filters.query, ignoreCase = true) ||
                                it.serialNumber.contains(filters.query, ignoreCase = true)
                    }
                }

                InventoryUiState.Success(
                    currentUser = currentUser,
                    equipment = finalFiltered,
                    departments = accessibleDepartments,
                    categories = allCategories,
                    selectedDepartment = filters.department,
                    searchQuery = filters.query,
                    selectedCategory = filters.category,
                    selectedStatus = filters.status,
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
        _filters.value = _filters.value.copy(query = query)
    }

    fun filterByDepartment(department: Department?) {
        _filters.value = _filters.value.copy(department = department)
    }

    fun filterByCategory(category: String?) {
        _filters.value = _filters.value.copy(category = category)
    }

    fun filterByStatus(status: EquipmentStatus?) {
        _filters.value = _filters.value.copy(status = status)
    }

    fun changeStatus(equipment: Equipment, newStatus: EquipmentStatus, notes: String) {
        viewModelScope.launch {
            changeEquipmentStatusUseCase(equipment, newStatus, notes)
        }
    }

    fun refresh() {
        loadInventory()
    }
}
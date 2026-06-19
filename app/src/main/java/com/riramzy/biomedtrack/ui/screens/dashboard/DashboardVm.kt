package com.riramzy.biomedtrack.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import com.riramzy.biomedtrack.domain.usecase.department.GetAllDepartmentsUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.GetAllEquipmentUseCase
import com.riramzy.biomedtrack.domain.usecase.statuschange.GetRecentStatusChangesUseCase
import com.riramzy.biomedtrack.domain.usecase.task.GetTasksUseCase
import com.riramzy.biomedtrack.utils.ActivityItem
import com.riramzy.biomedtrack.utils.ActivityType
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.TaskStatus
import com.riramzy.biomedtrack.utils.Timestamps.toDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DashboardUiState {
    data object Loading: DashboardUiState()
    data class Success(
        val currentUser: Technician,
        val stats: DashboardStats,
        val recentActivities: List<ActivityItem>,
        val upcomingMaintenance: List<Task>,
        val allDepartments: List<Department>
    ): DashboardUiState()
    data class Error(val message: String): DashboardUiState()
}

data class DashboardStats(
    val total: Int = 0,
    val online: Int = 0,
    val dueService: Int = 0,
    val down: Int = 0
)

@HiltViewModel
class DashboardVm @Inject constructor(
    private val getAllEquipmentsUseCase: GetAllEquipmentUseCase,
    private val getRecentStatusChangesUseCase: GetRecentStatusChangesUseCase,
    private val getAllDepartmentsUseCase: GetAllDepartmentsUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val maintenanceRepo: MaintenanceRepo,
    private val sessionManager: SessionManager
): ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        fetchDashboardData()
    }

    fun refresh() {
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        fetchJob?.cancel()
        _uiState.value = DashboardUiState.Loading

        fetchJob = viewModelScope.launch {
            val equipmentAndDepartmentsFlow = combine(
                getAllEquipmentsUseCase(),
                getAllDepartmentsUseCase()
            ) { equipment, departments ->
                Pair(equipment, departments)
            }

            combine(
                equipmentAndDepartmentsFlow,
                getRecentStatusChangesUseCase(20),
                maintenanceRepo.getAllMaintenanceLogs(),
                sessionManager.currentUser,
                getTasksUseCase()
            ) { equipmentAndDepartments, statusLogs, maintenanceLogs, user, tasks ->
                val (equipmentList, departmentsList) = equipmentAndDepartments
                val currentUser = user ?: return@combine DashboardUiState.Error("Session Expired")

                val isTechnician =
                    currentUser.role == com.riramzy.biomedtrack.utils.UserRole.TECHNICIAN
                val assignedDeptNames = currentUser.assignedDepartments.map { it.name }.toSet()
                val assignedDeptIds = currentUser.assignedDepartments.map { it.id }.toSet()

                val filteredEquipment = if (isTechnician) {
                    equipmentList.filter { it.department.id in assignedDeptIds || it.department.name in assignedDeptNames }
                } else {
                    equipmentList
                }

                val total = filteredEquipment.count()
                val online = filteredEquipment.count { it.status == EquipmentStatus.ONLINE }
                val dueService = filteredEquipment.count { it.status == EquipmentStatus.SERVICE }
                val down = filteredEquipment.count { it.status == EquipmentStatus.DOWN }

                val filteredDepartments = if (isTechnician) {
                    departmentsList.filter { it.id in assignedDeptIds || it.name in assignedDeptNames }
                } else {
                    departmentsList
                }

                val upcomingTasks = tasks
                    .filter { it.status != TaskStatus.DONE }
                    .sortedBy { it.dueDate }
                    .take(5)

                val changesList = statusLogs.map { statusLog ->
                    ActivityItem(
                        id = statusLog.id,
                        type = ActivityType.STATUS_CHANGE,
                        title = "Status Changed to ${statusLog.newStatus}",
                        equipmentId = statusLog.equipmentId,
                        equipmentName = statusLog.equipmentName,
                        equipmentModel = statusLog.equipmentModel,
                        equipmentSerial = statusLog.equipmentSerial,
                        departmentName = statusLog.department.name,
                        technicianName = statusLog.changedByName,
                        timestamp = statusLog.timestamp,
                        equipmentStatus = statusLog.newStatus,
                        previousStatus = statusLog.previousStatus,
                        dueDate = statusLog.timestamp.toDateString()
                    )
                }.let { list ->
                    if (isTechnician) list.filter { it.departmentName in assignedDeptNames }
                    else list
                }

                val logsList = maintenanceLogs.map { maintenanceLog ->
                    ActivityItem(
                        id = maintenanceLog.id,
                        type = ActivityType.MAINTENANCE_LOG,
                        title = "Maintenance Logged",
                        equipmentId = maintenanceLog.equipmentId,
                        equipmentName = maintenanceLog.equipmentName,
                        equipmentModel = maintenanceLog.equipmentModel,
                        equipmentSerial = maintenanceLog.equipmentSerial,
                        departmentName = maintenanceLog.department.name,
                        technicianName = maintenanceLog.technicianName,
                        timestamp = maintenanceLog.date,
                        equipmentStatus = maintenanceLog.currentStatus,
                        dueDate = maintenanceLog.date.toDateString()
                    )
                }.let { list ->
                    if (isTechnician) list.filter { it.departmentName in assignedDeptNames }
                    else list
                }

                val recentUnifiedActivities = (changesList + logsList)
                    .sortedByDescending { it.timestamp }
                    .take(5)

                DashboardUiState.Success(
                    currentUser = currentUser,
                    stats = DashboardStats(
                        total = total,
                        online = online,
                        dueService = dueService,
                        down = down
                    ),
                    recentActivities = recentUnifiedActivities,
                    upcomingMaintenance = upcomingTasks,
                    allDepartments = filteredDepartments
                )
            }.catch {
                _uiState.value = DashboardUiState.Error(it.message ?: "Failed to connect to database")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
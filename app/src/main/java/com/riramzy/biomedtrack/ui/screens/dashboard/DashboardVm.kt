package com.riramzy.biomedtrack.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import com.riramzy.biomedtrack.domain.usecase.department.GetAllDepartmentsUseCase
import com.riramzy.biomedtrack.domain.usecase.equipment.GetAllEquipmentUseCase
import com.riramzy.biomedtrack.domain.usecase.statuschange.GetRecentStatusChangesUseCase
import com.riramzy.biomedtrack.utils.ActivityItem
import com.riramzy.biomedtrack.utils.ActivityType
import com.riramzy.biomedtrack.utils.EquipmentStatus
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
        val upcomingMaintenance: List<Equipment>,
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
            combine(
                flow = getAllEquipmentsUseCase(),
                flow2 = getRecentStatusChangesUseCase(20),
                flow3 = maintenanceRepo.getAllMaintenanceLogs(),
                flow4 = getAllDepartmentsUseCase(),
                flow5 = sessionManager.currentUser
            ) { equipmentList, statusLogs, maintenanceLogs, departmentsList, user ->
                val total = equipmentList.count()

                val online = equipmentList.count {
                    it.status == EquipmentStatus.ONLINE
                }

                val dueService = equipmentList.count {
                    it.status == EquipmentStatus.SERVICE
                }

                val down = equipmentList.count {
                    it.status == EquipmentStatus.DOWN
                }

                val upcomingMaintenance = equipmentList
                    .filter { it.nextMaintenanceDate != null }
                    .sortedBy { it.nextMaintenanceDate }
                    .take(5)

                val currentUser = user ?: return@combine DashboardUiState.Error("Session Expired")

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
                        dueDate = statusLog.timestamp.toDateString()
                    )
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
                    upcomingMaintenance = upcomingMaintenance,
                    allDepartments = departmentsList
                )
            }.catch {
                _uiState.value = DashboardUiState.Error(it.message ?: "Failed to connect to database")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
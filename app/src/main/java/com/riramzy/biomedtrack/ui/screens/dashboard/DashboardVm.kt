package com.riramzy.biomedtrack.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.usecase.equipment.GetAllEquipmentUseCase
import com.riramzy.biomedtrack.domain.usecase.statuschange.GetRecentStatusChangesUseCase
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
        val recentActivities: List<StatusChangeLog>,
        val upcomingMaintenance: List<Equipment>
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
                getAllEquipmentsUseCase(),
                getRecentStatusChangesUseCase(20),
                sessionManager.currentUser
            ) { equipmentList, statusLogs, user ->
                val total = equipmentList.count()

                val online = equipmentList.count {
                    it.status == EquipmentStatus.ONLINE
                }

                val dueService = equipmentList.count {
                    it.status == EquipmentStatus.DUE_SERVICE
                }

                val down = equipmentList.count {
                    it.status == EquipmentStatus.DOWN
                }

                val upcomingMaintenance = equipmentList
                    .filter { it.nextMaintenanceDate != null }
                    .sortedBy { it.nextMaintenanceDate }
                    .take(5)

                val currentUser = user ?: return@combine DashboardUiState.Error("Session Expired")

                DashboardUiState.Success(
                    currentUser = currentUser,
                    stats = DashboardStats(
                        total = total,
                        online = online,
                        dueService = dueService,
                        down = down
                    ),
                    recentActivities = statusLogs,
                    upcomingMaintenance = upcomingMaintenance
                )
            }.catch {
                _uiState.value = DashboardUiState.Error(it.message ?: "Failed to connect to database")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
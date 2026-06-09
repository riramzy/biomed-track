package com.riramzy.biomedtrack.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import com.riramzy.biomedtrack.domain.repo.StatusChangeRepo
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import com.riramzy.biomedtrack.utils.ActivityItem
import com.riramzy.biomedtrack.utils.ActivityType
import com.riramzy.biomedtrack.utils.NotificationHeader
import com.riramzy.biomedtrack.utils.Timestamps.getGroupHeader
import com.riramzy.biomedtrack.utils.Timestamps.toDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NotificationsUiState {
    data object Loading: NotificationsUiState()
    data class Success(
        val notifications: Map<NotificationHeader, List<ActivityItem>> = emptyMap(),
        val unreadCount: Int = 0,
        val selectedCategory: NotificationCategory = NotificationCategory.ALL,
        val isLoading: Boolean = false
    ): NotificationsUiState()
    data class Error(val message: String): NotificationsUiState()
}

enum class NotificationCategory(val stringResId: Int) {
    ALL(R.string.category_all),
    STATUS_CHANGES(R.string.category_status_changes),
    MAINTENANCE_LOGS(R.string.category_maintenance_logs),
    SERVICE_REMINDERS(R.string.category_service_reminders)
}

sealed class NotificationsAction {
    data class SelectCategory(val category: NotificationCategory) : NotificationsAction()
    data class MarkAsRead(val item: ActivityItem): NotificationsAction()
    data object MarkAllAsRead: NotificationsAction()
    data object Refresh: NotificationsAction()
}

@HiltViewModel
class NotificationsVm @Inject constructor(
    private val statusChangeRepo: StatusChangeRepo,
    private val maintenanceRepo: MaintenanceRepo,
    private val tasksRepo: TaskRepo,
    sessionManager: SessionManager
): ViewModel() {
    private val _uiState = MutableStateFlow<NotificationsUiState>(NotificationsUiState.Loading)
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()
    private val _selectedCategory = MutableStateFlow(NotificationCategory.ALL)
    val user = sessionManager.currentUser.value

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            if (user == null) {
                NotificationsUiState.Error("Session expired")
                return@launch
            }

            combine(
                statusChangeRepo.getRecentStatusChanges(15),
                maintenanceRepo.getAllMaintenanceLogs(),
                tasksRepo.getAllTasks(),
                _selectedCategory
            ) { changes, logs, tasks, category ->
                val changesList = changes.map { log ->
                    ActivityItem(
                        id = log.id,
                        title = "Status changed from ${log.previousStatus} to ${log.newStatus}",
                        equipmentId = log.equipmentId,
                        equipmentName = log.equipmentName,
                        equipmentModel = log.equipmentModel,
                        equipmentSerial = log.equipmentSerial,
                        departmentName = log.department.name,
                        technicianName = log.changedByName,
                        type = ActivityType.STATUS_CHANGE,
                        timestamp = log.timestamp,
                        equipmentStatus = log.newStatus,
                        previousStatus = log.previousStatus,
                        isRead = log.readBy.contains(user.id)
                    )
                }.sortedByDescending { it.timestamp }

                val logsList = logs.map { log ->
                    ActivityItem(
                        id = log.id,
                        title = "Maintenance Logged",
                        equipmentId = log.equipmentId,
                        equipmentName = log.equipmentName,
                        equipmentModel = log.equipmentModel,
                        equipmentSerial = log.equipmentSerial,
                        departmentName = log.department.name,
                        technicianName = log.technicianName,
                        type = ActivityType.MAINTENANCE_LOG,
                        timestamp = log.date,
                        equipmentStatus = log.currentStatus,
                        isRead = log.readBy.contains(user.id)
                    )
                }.sortedByDescending { it.timestamp }

                val tasksList = tasks.map { task ->
                    ActivityItem(
                        id = task.id,
                        title = "Task Assigned to ${task.assignedToName}",
                        equipmentId = task.equipmentId,
                        equipmentName = task.equipmentName,
                        equipmentModel = task.equipmentModel,
                        equipmentSerial = task.equipmentSerial,
                        departmentName = task.department.name,
                        technicianName = task.assignedBy,
                        type = ActivityType.TASK_ASSIGNED,
                        timestamp = task.dueDate,
                        dueDate = task.dueDate.toDateString(),
                        taskStatus = task.status,
                        taskAssigneeName = task.assignedToName,
                        isRead = task.readBy.contains(user.id),
                    )
                }

                val categorizedList = (changesList + logsList +tasksList)
                    .filter {
                        when(category) {
                            NotificationCategory.ALL -> true
                            NotificationCategory.STATUS_CHANGES -> it.type == ActivityType.STATUS_CHANGE
                            NotificationCategory.MAINTENANCE_LOGS -> it.type == ActivityType.MAINTENANCE_LOG
                            NotificationCategory.SERVICE_REMINDERS -> it.type == ActivityType.TASK_ASSIGNED
                        }
                    }

                val notificationsList = categorizedList
                    .sortedByDescending {
                        it.timestamp
                    }
                    .groupBy { it.timestamp.getGroupHeader() }

                NotificationsUiState.Success(
                    notifications = notificationsList,
                    unreadCount = (changesList + logsList + tasksList).count { !it.isRead },
                    selectedCategory = category,
                    isLoading = false
                )
            }.catch { e ->
                _uiState.value = NotificationsUiState.Error(message = e.message ?: "Failed to connect to database")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onAction(action: NotificationsAction) {
        when(action) {
            is NotificationsAction.SelectCategory -> {
                _selectedCategory.value = action.category
            }
            is NotificationsAction.MarkAllAsRead -> {
                val currentState = _uiState.value
                if (currentState is NotificationsUiState.Success && user != null) {
                    viewModelScope.launch {
                        val unreadItems = currentState.notifications.values.flatten().filter { !it.isRead }

                        unreadItems.forEach { item ->
                            when(item.type) {
                                ActivityType.STATUS_CHANGE -> {
                                    statusChangeRepo.markAsRead(item.id, user.id)
                                }
                                ActivityType.MAINTENANCE_LOG -> {
                                    maintenanceRepo.markAsRead(item.id, user.id)
                                }
                                ActivityType.TASK_ASSIGNED -> {
                                    tasksRepo.markAsRead(item.id, user.id)
                                }
                            }
                        }
                    }
                }
            }
            is NotificationsAction.MarkAsRead -> {
                val currentState = _uiState.value
                if (currentState is NotificationsUiState.Success && user != null) {
                    viewModelScope.launch {
                        val item = action.item
                        when (item.type) {
                            ActivityType.STATUS_CHANGE -> {
                                statusChangeRepo.markAsRead(item.id, user.id)
                            }

                            ActivityType.MAINTENANCE_LOG -> {
                                maintenanceRepo.markAsRead(item.id, user.id)
                            }

                            ActivityType.TASK_ASSIGNED -> {
                                tasksRepo.markAsRead(item.id, user.id)
                            }
                        }
                    }
                }
            }
            is NotificationsAction.Refresh -> {
                viewModelScope.launch {
                    loadNotifications()
                }
            }
        }
    }
}
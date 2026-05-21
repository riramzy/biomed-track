package com.riramzy.biomedtrack.ui.screens.scheduler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.usecase.task.GetTasksUseCase
import com.riramzy.biomedtrack.utils.TaskStatus
import com.riramzy.biomedtrack.utils.Timestamps.getEndOfWeek
import com.riramzy.biomedtrack.utils.Timestamps.getStartOfWeek
import com.riramzy.biomedtrack.utils.Timestamps.isOverdue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

sealed class SchedulerUiState {
    data object Loading : SchedulerUiState()
    data class Success(
        val tasksByDay: Map<Long, List<Task>>,
        val overdueTasks: List<Task>,
        val allUpcomingTasks: List<Task>,
        val currentWeekOffset: Int,
        val isListView: Boolean,
        val weekRangeText: String,
        val isCustomRangeActive: Boolean = false
    ) : SchedulerUiState()
    data class Error(val message: String) : SchedulerUiState()
}

@HiltViewModel
class SchedulerVm @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<SchedulerUiState>(SchedulerUiState.Loading)
    val uiState: StateFlow<SchedulerUiState> = _uiState.asStateFlow()

    private val _weekOffset = MutableStateFlow(0)
    private val _isListView = MutableStateFlow(false)
    private val _customDateRange = MutableStateFlow<Pair<Long, Long>?>(null)

    init {
        viewModelScope.launch {
            combine(
                getTasksUseCase(),
                _weekOffset,
                _isListView,
                _customDateRange
            ) { tasks, offset, isListView, customRange ->
                val start = customRange?.first ?: getStartOfWeek(offset)
                val end = customRange?.second ?: getEndOfWeek(offset)

                // Convert start and end to LocalDate in local zone to avoid timezone/DST drift
                val startLocal = Instant.ofEpochMilli(start).atZone(ZoneId.systemDefault()).toLocalDate()
                val endLocal = Instant.ofEpochMilli(end).atZone(ZoneId.systemDefault()).toLocalDate()

                val overdue = tasks.filter {
                    it.dueDate.isOverdue() && it.status != TaskStatus.DONE
                }.sortedByDescending { it.dueDate }

                // Filter tasks within the local range (inclusive)
                val rangeTasks = tasks.filter {
                    val taskLocalDate = Instant.ofEpochMilli(it.dueDate).atZone(ZoneId.systemDefault()).toLocalDate()
                    !taskLocalDate.isBefore(startLocal) && !taskLocalDate.isAfter(endLocal)
                }.sortedBy { it.dueDate }

                // Grouping by day using robust LocalDate progression
                val groupedTasks = mutableMapOf<Long, List<Task>>()
                val daysCount = java.time.temporal.ChronoUnit.DAYS.between(startLocal, endLocal).toInt().coerceIn(0, 365)

                for (i in 0..daysCount) {
                    val currentLocalDate = startLocal.plusDays(i.toLong())
                    val dayStart = currentLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    groupedTasks[dayStart] = rangeTasks.filter {
                        val taskLocalDate = Instant.ofEpochMilli(it.dueDate).atZone(ZoneId.systemDefault()).toLocalDate()
                        taskLocalDate == currentLocalDate
                    }
                }

                val upcomingTasks = if (customRange != null) {
                    rangeTasks.filter { it.status != TaskStatus.DONE }
                } else {
                    tasks.filter { 
                        it.dueDate >= System.currentTimeMillis() && it.status != TaskStatus.DONE 
                    }.sortedBy { it.dueDate }
                }

                SchedulerUiState.Success(
                    tasksByDay = groupedTasks,
                    overdueTasks = overdue,
                    allUpcomingTasks = upcomingTasks,
                    currentWeekOffset = offset,
                    isListView = isListView,
                    weekRangeText = formatWeekRange(start, end),
                    isCustomRangeActive = customRange != null
                )
            }.catch { e ->
                _uiState.value = SchedulerUiState.Error(e.message ?: "Failed to load tasks")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun nextWeek() {
        _customDateRange.value = null
        _weekOffset.value += 1
    }

    fun currentWeek() {
        _customDateRange.value = null
        _weekOffset.value = 0
    }

    fun previousWeek() {
        _customDateRange.value = null
        _weekOffset.value -= 1
    }

    fun toggleViewMode() {
        _isListView.value = !_isListView.value
    }

    fun setCustomDateRange(start: Long, end: Long) {
        _customDateRange.value = Pair(start, end)
    }

    fun clearCustomDateRange() {
        _customDateRange.value = null
    }

    private fun formatWeekRange(start: Long, end: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
        return "${sdf.format(java.util.Date(start))} - ${sdf.format(java.util.Date(end))}"
    }
}
package com.riramzy.biomedtrack.ui.screens.scheduler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import com.riramzy.biomedtrack.domain.usecase.equipment.GetAllEquipmentUseCase
import com.riramzy.biomedtrack.domain.usecase.task.AddTaskUseCase
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.TaskStatus
import com.riramzy.biomedtrack.utils.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ScheduleMaintenanceUiState(
    val equipmentList: List<Equipment> = emptyList(),
    val techniciansList: List<Technician> = emptyList(),
    val selectedEquipment: Equipment? = null,
    val selectedDate: Long = System.currentTimeMillis(),
    val selectedTechnician: Technician? = null,
    val notes: String = "",
    val checklist: List<ChecklistItem> = listOf(
        ChecklistItem("1", "Visual Inspection", true),
        ChecklistItem("2", "Electrical Safety Check", true),
        ChecklistItem("3", "Cleaning & Lubrication", true),
        ChecklistItem("4", "Functional Performance Test", true),
        ChecklistItem("5", "Calibration & Alignment", true),
        ChecklistItem("6", "Wear Parts Replacement", false),
        ChecklistItem("7", "Software & Firmware Check", false)
    ),
    val isLoading: Boolean = false,
    val isError: String? = null,
    val isSuccess: Boolean = false,
)

sealed class ScheduleMaintenanceAction {
    data class SelectEquipment(val equipment: Equipment) : ScheduleMaintenanceAction()
    data class SelectDate(val date: Long) : ScheduleMaintenanceAction()
    data class SelectTechnician(val technicianName: String) : ScheduleMaintenanceAction()
    data class UpdateNotes(val notes: String) : ScheduleMaintenanceAction()
    data class ToggleChecklistItem(val item: ChecklistItem) : ScheduleMaintenanceAction()
    object ResetError : ScheduleMaintenanceAction()
    object ScheduleTask : ScheduleMaintenanceAction()
}

@HiltViewModel
class ScheduleMaintenanceVm @Inject constructor(
    private val getAllEquipmentUseCase: GetAllEquipmentUseCase,
    private val authRepo: AuthRepo,
    private val addTasksUseCase: AddTaskUseCase,
    private val sessionManager: SessionManager
): ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleMaintenanceUiState())
    val uiState: StateFlow<ScheduleMaintenanceUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                getAllEquipmentUseCase(),
                authRepo.getAllUsers()
            ) { allEquipment, users ->
                val activeUsers = users.filter {
                    it.isActive && it.role == UserRole.TECHNICIAN
                }

                Pair(allEquipment, activeUsers)
            }.catch { e ->
                _uiState.value = _uiState.value.copy(
                    isError = e.message,
                    isLoading = false
                )
            }.collect { (equipment, technicians) ->
                _uiState.value = _uiState.value.copy(
                    equipmentList = equipment,
                    techniciansList = technicians,
                    isLoading = false
                )
            }
        }
    }

    private fun scheduleTask() {
        val currentState = _uiState.value
        val selectedEquipment = currentState.selectedEquipment
        val selectedTechnician = currentState.selectedTechnician

        if (selectedEquipment == null) {
            _uiState.update { it.copy(isError = "Please select an equipment", isLoading = false) }
            return
        }

        if (selectedTechnician == null) {
            _uiState.update { it.copy(isError = "Please select a technician", isLoading = false) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val currentUser = sessionManager.currentUser.value

            if (currentUser == null) {
                _uiState.update { it.copy(isError = "User not logged in", isLoading = false) }
                return@launch
            }

            val newTask = Task(
                id = UUID.randomUUID().toString(),
                equipmentId = selectedEquipment.id,
                equipmentName = selectedEquipment.name,
                equipmentModel = selectedEquipment.model,
                equipmentSerial = selectedEquipment.serialNumber,
                dueDate = currentState.selectedDate,
                status = TaskStatus.PENDING,
                notes = currentState.notes,
                department = selectedEquipment.department,
                assignedTo = selectedTechnician.id,
                assignedToName = selectedTechnician.name,
                assignedBy = currentUser.name,
                scheduledChecklist = currentState.checklist
            )

            when (val result = addTasksUseCase(newTask)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isSuccess = true,
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isError = result.message,
                            isLoading = false
                        )
                    }
                }

                is Result.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun onAction(action: ScheduleMaintenanceAction) {
        when (action) {
            is ScheduleMaintenanceAction.SelectEquipment -> {
                _uiState.update {
                    it.copy(selectedEquipment = action.equipment)
                }
            }

            is ScheduleMaintenanceAction.SelectDate -> {
                _uiState.update {
                    it.copy(selectedDate = action.date)
                }
            }

            is ScheduleMaintenanceAction.SelectTechnician -> {
                val selectedTechnician = _uiState.value.techniciansList.find {
                    it.name == action.technicianName
                }
                _uiState.update {
                    it.copy(selectedTechnician = selectedTechnician)
                }
            }

            is ScheduleMaintenanceAction.UpdateNotes -> {
                _uiState.update {
                    it.copy(notes = action.notes)
                }
            }

            is ScheduleMaintenanceAction.ToggleChecklistItem -> {
                val updatedChecklist = _uiState.value.checklist.map {
                    if (it.id == action.item.id) {
                        it.copy(isChecked = !it.isChecked)
                    } else {
                        it
                    }
                }
                _uiState.update {
                    it.copy(checklist = updatedChecklist)
                }
            }

            is ScheduleMaintenanceAction.ResetError -> {
                _uiState.update {
                    it.copy(isError = null)
                }
            }

            is ScheduleMaintenanceAction.ScheduleTask -> {
                scheduleTask()
            }
        }
    }
}
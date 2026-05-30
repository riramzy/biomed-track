package com.riramzy.biomedtrack.domain.usecase.maintenance

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import com.riramzy.biomedtrack.domain.repo.StatusChangeRepo
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.TaskStatus
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import javax.inject.Inject

class AddMaintenanceLogUseCase @Inject constructor(
    private val maintenanceRepository: MaintenanceRepo,
    private val equipmentRepository: EquipmentRepo,
    private val statusChangeRepo: StatusChangeRepo,
    private val taskRepo: TaskRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(log: MaintenanceLog, taskId: String?): Result<Unit> {
        val user = sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!sessionManager.hasPermission(Permission.ADD_MAINTENANCE_LOG)) {
            return Result.Error("User doesn't have permission to log maintenance")
        }

        if (!sessionManager.canWriteToDepartment(log.department)) {
            return Result.Error("User does not have permission to write to this department")
        }

        if (log.technicianName.isBlank()) {
            return Result.Error("Technician name cannot be empty")
        }

        val equipment = equipmentRepository.getEquipmentById(log.equipmentId)
            ?: return Result.Error("Equipment not found")

        val nextMaintenanceDate = LocalDate.now()
            .plusDays(equipment.serviceIntervalDays.toLong())

        val updatedEquipmentResult = equipmentRepository.updateEquipment(
            equipment.copy(
                lastMaintenanceDate = log.date,
                nextMaintenanceDate = nextMaintenanceDate.toEpochDay(),
                status = log.currentStatus
            )
        )
        if (updatedEquipmentResult is Result.Error) {
            return Result.Error("Failed to update equipment")
        }

        if (!taskId.isNullOrBlank()) {
            val tasksSnapshot = taskRepo.getTasksForTechnician(user.id).firstOrNull()

            val activeTask = tasksSnapshot?.find { it.id == taskId }

            if (activeTask != null) {
                val updatedTaskResult = taskRepo.updateTask(activeTask.copy(status = TaskStatus.DONE))

                if (updatedTaskResult is Result.Error) {
                    return Result.Error("Failed to update scheduled task status")
                }
            }
        }

        if (equipment.status != log.currentStatus) {
            val newStatusLogEntry = StatusChangeLog(
                id = log.id,
                equipmentId = equipment.id,
                equipmentName = equipment.name,
                equipmentModel = equipment.model,
                equipmentSerial = equipment.serialNumber,
                department = equipment.department,
                previousStatus = equipment.status,
                newStatus = log.currentStatus,
                changedBy = user.id,
                changedByName = user.name,
                timestamp = System.currentTimeMillis(),
                notes = "Completed scheduled maintenance task"
            )

            statusChangeRepo.logStatusChange(newStatusLogEntry)
        }

        val logMaintenanceResult = maintenanceRepository.addLog(log)
        if (logMaintenanceResult is Result.Error) {
            return Result.Error("Failed to log maintenance")
        }

        return Result.Success(Unit)
    }
}

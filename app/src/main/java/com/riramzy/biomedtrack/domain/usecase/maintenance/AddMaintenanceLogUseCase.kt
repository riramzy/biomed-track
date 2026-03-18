package com.riramzy.biomedtrack.domain.usecase.maintenance

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.Result
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import java.time.LocalDate
import javax.inject.Inject

class AddMaintenanceLogUseCase @Inject constructor(
    private val maintenanceRepository: MaintenanceRepo,
    private val equipmentRepository: EquipmentRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(log: MaintenanceLog): Result<Unit> {
        sessionManager.currentUser.value ?: return Result.Error("User not logged in")

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
            .toString()

        val updatedEquipmentResult = equipmentRepository.updateEquipment(
            equipment.copy(
                lastMaintenanceDate = log.date,
                nextMaintenanceDate = nextMaintenanceDate
            )
        )
        if (updatedEquipmentResult is Result.Error) {
            return Result.Error("Failed to update equipment")
        }

        val logMaintenanceResult = maintenanceRepository.addLog(log)
        if (logMaintenanceResult is Result.Error) {
            return Result.Error("Failed to log maintenance")
        }

        return Result.Success(Unit)
    }
}

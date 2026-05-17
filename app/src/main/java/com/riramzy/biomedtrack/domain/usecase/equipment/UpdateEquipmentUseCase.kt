package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.permission.hasPermission
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import com.riramzy.biomedtrack.domain.repo.StatusChangeRepo
import java.util.UUID
import javax.inject.Inject

class UpdateEquipmentUseCase @Inject constructor(
    private val equipmentRepository: EquipmentRepo,
    private val statusChangeRepo: StatusChangeRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(equipment: Equipment): Result<Unit> {
        val currentUser = sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!currentUser.role.hasPermission(Permission.EDIT_EQUIPMENT)) {
            return Result.Error("User does not have permission to edit equipment")
        }

        if (equipment.id.isBlank()) {
            return Result.Error("Equipment id cannot be blank")
        }

        val oldEquipment = equipmentRepository.getEquipmentById(equipment.id)
            ?: return Result.Error("Equipment not found")

        val updateResult = equipmentRepository.updateEquipment(equipment)

        if (updateResult is Result.Error) return updateResult

        if (oldEquipment.status != equipment.status) {
            val newStatusLogEntry = StatusChangeLog(
                id = UUID.randomUUID().toString(),
                equipmentId = equipment.id,
                equipmentName = equipment.name,
                equipmentModel = equipment.model,
                equipmentSerial = equipment.serialNumber,
                department = equipment.department,
                previousStatus = oldEquipment.status,
                newStatus = equipment.status,
                changedBy = currentUser.id,
                changedByName = currentUser.name,
                timestamp = System.currentTimeMillis(),
                notes = "Updated equipment via the edit screen"
            )

            statusChangeRepo.logStatusChange(newStatusLogEntry)
        }

        return updateResult
    }
}
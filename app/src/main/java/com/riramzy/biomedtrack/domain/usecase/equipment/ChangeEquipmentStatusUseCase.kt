package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import com.riramzy.biomedtrack.domain.repo.StatusChangeRepo
import java.util.UUID
import javax.inject.Inject

class ChangeEquipmentStatusUseCase @Inject constructor(
    private val equipmentRepository: EquipmentRepo,
    private val statusChangeRepo: StatusChangeRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(
        equipment: Equipment,
        newStatus: EquipmentStatus,
        notes: String? = null
    ): Result<Unit> {
        val currentUser = sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!sessionManager.canWriteToDepartment(equipment.department)) {
            return Result.Error("You are not assigned to this department")
        }

        val statusChangeLog = StatusChangeLog(
            id = UUID.randomUUID().toString(),
            equipmentId = equipment.id,
            equipmentName = equipment.name,
            equipmentModel = equipment.model,
            equipmentSerial = equipment.serialNumber,
            department = equipment.department,
            previousStatus = equipment.status,
            newStatus = newStatus,
            changedBy = currentUser.id,
            changedByName = currentUser.name,
            timestamp = System.currentTimeMillis(),
            notes = notes
        )

        val updateResult = equipmentRepository.updateEquipment(equipment.copy(status = newStatus))
        if (updateResult is Result.Error) {
            return updateResult
        }

        val logResult = statusChangeRepo.logStatusChange(statusChangeLog)
        if (logResult is Result.Error) {
            return logResult
        }

        return Result.Success(Unit)
    }
}
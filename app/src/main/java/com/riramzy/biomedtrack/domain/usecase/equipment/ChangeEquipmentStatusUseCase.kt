package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.Result
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.EquipmentStatus
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
            equipmentId = equipment.id,
            equipmentName = equipment.name,
            previousStatus = equipment.status,
            newStatus = newStatus,
            notes = notes,
            changedBy = currentUser.id,
            changedByName = currentUser.name,
            department = equipment.department,
            timestamp = System.currentTimeMillis().toString(),
            id = UUID.randomUUID().toString(),
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
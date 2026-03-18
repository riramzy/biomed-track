package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.Result
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.permission.hasPermission
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import javax.inject.Inject

class DeleteEquipmentUseCase @Inject constructor(
    private val equipmentRepository: EquipmentRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(equipmentId: String): Result<Unit> {
        val currentUser = sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!currentUser.role.hasPermission(Permission.DELETE_EQUIPMENT)) {
            return Result.Error("User does not have permission to delete equipment")
        }

        if (equipmentId.isBlank()) {
            return Result.Error("Equipment id cannot be blank")
        }

        return equipmentRepository.deleteEquipment(equipmentId)
    }
}
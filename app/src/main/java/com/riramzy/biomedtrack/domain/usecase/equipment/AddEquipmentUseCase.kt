package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.permission.hasPermission
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import javax.inject.Inject

class AddEquipmentUseCase @Inject constructor(
    private val equipmentRepository: EquipmentRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(equipment: Equipment): Result<Unit> {
        val currentUser = sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!currentUser.role.hasPermission(Permission.ADD_EQUIPMENT)) {
            return Result.Error("User does not have permission to add equipment")
        }

        if (equipment.name.isBlank()) {
            return Result.Error("Equipment name cannot be blank")
        }

        if (equipment.serialNumber.isBlank()) {
            return Result.Error("Equipment serial number cannot be blank")
        }

        if (equipment.manufacturer.isBlank()) {
            return Result.Error("Equipment manufacturer cannot be blank")
        }

        if (equipment.agent.isBlank()) {
            return Result.Error("Equipment agent cannot be blank")
        }

        if (equipment.category.isBlank()) {
            return Result.Error("Equipment category cannot be blank")
        }

        if (equipment.location.isBlank()) {
            return Result.Error("Equipment location cannot be blank")
        }

        if (equipment.installDate == 0L) {
            return Result.Error("Equipment install date cannot be blank")
        }

        return equipmentRepository.addEquipment(equipment)
    }
}
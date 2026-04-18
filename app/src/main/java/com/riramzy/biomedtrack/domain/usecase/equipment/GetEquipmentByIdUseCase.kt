package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import javax.inject.Inject

class GetEquipmentByIdUseCase @Inject constructor(
    private val equipmentRepo: EquipmentRepo
) {
    suspend operator fun invoke(id: String): Equipment? {
        return equipmentRepo.getEquipmentById(id = id)
    }
}
package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllEquipmentUseCase @Inject constructor(
    private val equipmentRepository: EquipmentRepo
) {
    operator fun invoke(): Flow<List<Equipment>> {
        return equipmentRepository.getAllEquipment()
    }
}
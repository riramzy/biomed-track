package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import javax.inject.Inject

class GetEquipmentByDepartmentUseCase @Inject constructor(
    private val equipmentRepo: EquipmentRepo
) {
    operator fun invoke(department: Department) = equipmentRepo.getEquipmentByDepartment(department)
}
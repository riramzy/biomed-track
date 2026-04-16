package com.riramzy.biomedtrack.domain.usecase.department

import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import jakarta.inject.Inject

class GetDepartmentByIdUseCase @Inject constructor(
    private val departmentRepo: DepartmentRepo
) {
    suspend operator fun invoke(id: String): Department? {
        return departmentRepo.getDepartmentById(id)
    }
}
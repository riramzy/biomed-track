package com.riramzy.biomedtrack.domain.usecase.department

import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import javax.inject.Inject

class GetAllDepartmentsOnceUseCase @Inject constructor(
    private val departmentRepo: DepartmentRepo
) {
    suspend operator fun invoke(): List<Department> {
        return departmentRepo.getAllDepartmentsOnce()
    }
}
package com.riramzy.biomedtrack.domain.usecase.department

import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import com.riramzy.biomedtrack.utils.Result
import jakarta.inject.Inject

class UpdateDepartmentUseCase @Inject constructor(
    private val departmentRepo: DepartmentRepo
) {
    suspend operator fun invoke(department: Department): Result<Unit> {
        return departmentRepo.updateDepartment(department)
    }
}
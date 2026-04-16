package com.riramzy.biomedtrack.domain.usecase.department

import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import javax.inject.Inject

class DeleteDepartmentUseCase @Inject constructor(
    private val departmentRepo: DepartmentRepo
) {
    suspend operator fun invoke(id: String) {
        departmentRepo.deleteDepartment(id)
    }
}
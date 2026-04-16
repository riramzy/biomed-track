package com.riramzy.biomedtrack.domain.usecase.department

import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import jakarta.inject.Inject

class GetDepartmentsCountUseCase @Inject constructor(
    private val departmentRepo: DepartmentRepo
) {
    suspend operator fun invoke(): Int {
        return departmentRepo.getDepartmentsCount()
    }
}
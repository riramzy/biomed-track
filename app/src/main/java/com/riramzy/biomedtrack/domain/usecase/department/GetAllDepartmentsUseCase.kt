package com.riramzy.biomedtrack.domain.usecase.department

import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDepartmentsUseCase @Inject constructor(
    private val departmentRepo: DepartmentRepo
) {
    operator fun invoke(): Flow<List<Department>> {
        return departmentRepo.getAllDepartments()
    }
}
package com.riramzy.biomedtrack.domain.repo

import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import kotlinx.coroutines.flow.Flow

interface DepartmentRepo {
    fun getAllDepartments(): Flow<List<Department>>
    suspend fun getAllDepartmentsOnce(): List<Department>
    suspend fun getDepartmentById(id: String): Department?
    suspend fun getDepartmentsCount(): Int
    suspend fun addDepartment(department: Department): Result<Unit>
    suspend fun updateDepartment(department: Department): Result<Unit>
    suspend fun deleteDepartment(id: String): Result<Unit>
}
package com.riramzy.biomedtrack.domain.repo

import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.utils.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun login(email: String, password: String): Result<Technician>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Technician?
    suspend fun createUser(technician: Technician, password: String): Result<Technician>
    fun getAllUsers(): Flow<List<Technician>>
    suspend fun updateUserRole(userId: String, role: UserRole): Result<Unit>
    suspend fun updateUserDepartments(userId: String, departments: List<Department>): Result<Unit>
    suspend fun deactivateUser(userId: String): Result<Unit>
    suspend fun activateUser(userId: String): Result<Unit>
    suspend fun updateFcmToken(userId: String, token: String): Result<Unit>
}
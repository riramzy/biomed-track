package com.riramzy.biomedtrack.domain.usecase.auth

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import jakarta.inject.Inject
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.permission.Permission

class CreateUserUseCase @Inject constructor(
    private val authRepository: AuthRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(technician: Technician, password: String): Result<Technician> {
        sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!sessionManager.hasPermission(Permission.MANAGE_USERS)) {
            return Result.Error("User doesn't have permission to create users")
        }

        if (technician.name.isBlank()) {
            return Result.Error("Technician name cannot be empty")
        }

        if (technician.email.isBlank()) {
            return Result.Error("Technician email cannot be empty")
        }

        if (password.length < 6) {
            return Result.Error("Password cannot be less than 6 characters")
        }

        return authRepository.createUser(technician, password)
    }
}
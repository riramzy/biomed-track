package com.riramzy.biomedtrack.domain.usecase.auth

import com.riramzy.biomedtrack.domain.repo.AuthRepo
import javax.inject.Inject
import com.riramzy.biomedtrack.utils.Result

class ChangePasswordUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {
    suspend operator fun invoke(oldPassword: String, newPassword: String): Result<Unit> {
        if (oldPassword.isBlank() || newPassword.isBlank()) {
            return Result.Error("Password cannot be empty")
        }

        if (oldPassword == newPassword) {
            return Result.Error("New password cannot be the same as the old password")
        }

        if (newPassword.length < 6) {
            return Result.Error("Password must be at least 6 characters long")
        }

        if (!newPassword.any { it.isUpperCase() }) {
            return Result.Error("Password must contain at least one uppercase letter")
        }

        if (!newPassword.any { it.isLowerCase() }) {
            return Result.Error("Password must contain at least one lowercase letter")
        }

        if (!newPassword.any { it.isDigit() }) {
            return Result.Error("Password must contain at least one digit")
        }

        return authRepo.changePassword(oldPassword, newPassword)
    }
}
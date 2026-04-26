package com.riramzy.biomedtrack.domain.usecase.auth

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import javax.inject.Inject
import com.riramzy.biomedtrack.utils.Result

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(email: String, password: String): Result<Technician> {
        if (email.isBlank()) return Result.Error("Email cannot be blank")

        if (password.isBlank()) return Result.Error("Password cannot be blank")

        val result = authRepository.login(email, password)
        if (result is Result.Success) {
            sessionManager.setUser(technician = result.data)
            return result
        } else {
            return result
        }
    }
}
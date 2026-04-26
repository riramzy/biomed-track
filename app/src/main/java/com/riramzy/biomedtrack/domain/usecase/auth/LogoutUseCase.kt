package com.riramzy.biomedtrack.domain.usecase.auth

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import jakarta.inject.Inject
import com.riramzy.biomedtrack.utils.Result

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Result<Unit> {
        val result = authRepository.logout()
        if (result is Result.Success) {
            sessionManager.clearUser()
        } else if (result is Result.Error) {
            return result
        }
        return result
    }
}
package com.riramzy.biomedtrack.domain.usecase.maintenance

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.Result
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import javax.inject.Inject

class DeleteMaintenanceLogUseCase @Inject constructor(
    private val maintenanceRepository: MaintenanceRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(logId: String): Result<Unit> {
        sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!sessionManager.hasPermission(Permission.DELETE_LOG)) {
            return Result.Error("User doesn't have permission to delete logs")
        }

        if (logId.isBlank()) {
            return Result.Error("Log id cannot be blank")
        }

        return maintenanceRepository.deleteLog(logId)
    }
}
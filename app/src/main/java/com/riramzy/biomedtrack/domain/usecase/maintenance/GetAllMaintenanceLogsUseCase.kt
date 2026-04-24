package com.riramzy.biomedtrack.domain.usecase.maintenance

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import com.riramzy.biomedtrack.utils.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetAllMaintenanceLogsUseCase @Inject constructor(
    private val maintenanceRepository: MaintenanceRepo,
    private val sessionManager: SessionManager
) {
    operator fun invoke(): Flow<List<MaintenanceLog>> {
        val currentUser = sessionManager.currentUser.value ?: return flowOf(emptyList())

        return if (currentUser.role == UserRole.TECHNICIAN) {
            maintenanceRepository.getAllMaintenanceLogs().map {
                    logs -> logs.filter { it.technicianId == currentUser.id }
            }
        } else {
            maintenanceRepository.getAllMaintenanceLogs()
        }
    }
}
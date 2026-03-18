package com.riramzy.biomedtrack.domain.usecase.maintenance

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.model.UserRole
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLogsForEquipmentUseCase @Inject constructor(
    private val maintenanceRepository: MaintenanceRepo,
    private val sessionManager: SessionManager
) {
    operator fun invoke(equipmentId: String): Flow<List<MaintenanceLog>> {
        val currentUser = sessionManager.currentUser.value ?: return flowOf(emptyList())

        return if (currentUser.role == UserRole.TECHNICIAN) {
            maintenanceRepository.getEquipmentLog(equipmentId).map {
                 logs -> logs.filter { it.technicianId == currentUser.id }
            }
        } else {
            maintenanceRepository.getEquipmentLog(equipmentId)
        }
    }
}
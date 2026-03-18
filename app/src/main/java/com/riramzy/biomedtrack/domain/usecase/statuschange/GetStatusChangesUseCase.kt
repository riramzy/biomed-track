package com.riramzy.biomedtrack.domain.usecase.statuschange

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.repo.StatusChangeRepo
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetStatusChangesUseCase @Inject constructor(
    private val statusChangeRepository: StatusChangeRepo,
    private val sessionManager: SessionManager
) {
    operator fun invoke(equipmentId: String): Flow<List<StatusChangeLog>> {
        sessionManager.currentUser.value ?: return emptyFlow()

        return statusChangeRepository.getStatusChangeForEquipment(equipmentId)
    }
}
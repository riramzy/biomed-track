package com.riramzy.biomedtrack.domain.repo

import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import kotlinx.coroutines.flow.Flow

interface StatusChangeRepo {
    fun getStatusChangeForEquipment(equipmentId: String): Flow<List<StatusChangeLog>>
    fun getRecentStatusChanges(limit: Int): Flow<List<StatusChangeLog>>
    suspend fun logStatusChange(statusChangeLog: StatusChangeLog): Result<Unit>
    suspend fun markAsRead(logId: String, userId: String): Result<Unit>
}
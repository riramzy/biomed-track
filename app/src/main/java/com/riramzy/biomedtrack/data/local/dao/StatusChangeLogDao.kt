package com.riramzy.biomedtrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riramzy.biomedtrack.data.local.entity.StatusChangeLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StatusChangeLogDao {
    @Query("SELECT * FROM status_change_log")
    fun getAllStatusChangeLogs(): Flow<List<StatusChangeLogEntity>>

    @Query("SELECT * FROM status_change_log WHERE equipmentId = :equipmentId")
    fun getStatusChangeLogsByEquipmentId(equipmentId: String): Flow<List<StatusChangeLogEntity>>

    @Query("SELECT * FROM status_change_log ORDER BY timestamp DESC LIMIT :limit")
    fun getStatusChangeLogsByDateRange(limit: Int): Flow<List<StatusChangeLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatusChangeLog(statusChangeLog: StatusChangeLogEntity)
}
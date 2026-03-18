package com.riramzy.biomedtrack.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.riramzy.biomedtrack.data.local.entity.MaintenanceLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceLogDao {
    @Query("SELECT * FROM maintenance_log")
    fun getAllMaintenanceLogs(): Flow<List<MaintenanceLogEntity>>

    @Query("SELECT * FROM maintenance_log WHERE technicianId = :technicianId")
    fun getMaintenanceLogsByTechnicianId(technicianId: String): Flow<List<MaintenanceLogEntity>>

    @Query("SELECT * FROM maintenance_log WHERE equipmentId = :equipmentId")
    fun getMaintenanceLogsByEquipmentId(equipmentId: String): Flow<List<MaintenanceLogEntity>>

    @Query("SELECT * FROM maintenance_log WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getMaintenanceLogsByDateRange(startDate: String, endDate: String): List<MaintenanceLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenanceLog(maintenanceLog: MaintenanceLogEntity)

    @Update
    suspend fun updateMaintenanceLog(maintenanceLog: MaintenanceLogEntity)

    @Query("DELETE FROM maintenance_log WHERE id = :maintenanceLogId")
    suspend fun deleteMaintenanceLog(maintenanceLogId: String)
}
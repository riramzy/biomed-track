package com.riramzy.biomedtrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.riramzy.biomedtrack.data.local.entity.EquipmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipment")
    fun getAllEquipment(): Flow<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment WHERE department = :department")
    fun getEquipmentByDepartment(department: String): Flow<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment WHERE id = :id")
    suspend fun getEquipmentById(id: String): EquipmentEntity?

    @Query("SELECT COUNT(*) FROM equipment WHERE status = 'ONLINE'")
    suspend fun getOnlineEquipmentCount(): Int

    @Query("SELECT COUNT(*) FROM equipment WHERE status = 'DOWN'")
    suspend fun getDownEquipmentCount(): Int

    @Query("SELECT COUNT(*) FROM equipment WHERE status = 'DUE_SERVICE'")
    suspend fun getDueServiceEquipmentCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(equipment: EquipmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEquipment(equipment: List<EquipmentEntity>)

    @Update
    suspend fun updateEquipment(equipment: EquipmentEntity)

    @Query("DELETE FROM equipment WHERE id = :id")
    suspend fun deleteEquipmentById(id: String)
}
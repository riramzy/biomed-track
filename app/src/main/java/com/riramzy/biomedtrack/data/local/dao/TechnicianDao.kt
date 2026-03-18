package com.riramzy.biomedtrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.riramzy.biomedtrack.data.local.entity.TechnicianEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TechnicianDao {
    @Query("SELECT * FROM technician")
    fun getAllTechnicians(): Flow<List<TechnicianEntity>>

    @Query("SELECT * FROM technician WHERE id = :id")
    suspend fun getTechnicianById(id: String): TechnicianEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechnician(technician: TechnicianEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTechnicians(technicians: List<TechnicianEntity>)

    @Update
    suspend fun updateTechnician(technician: TechnicianEntity)

    @Query("DELETE FROM technician WHERE id = :id")
    suspend fun deleteTechnicianById(id: String)
}
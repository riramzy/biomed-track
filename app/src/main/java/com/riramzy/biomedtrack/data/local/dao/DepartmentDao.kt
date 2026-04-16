package com.riramzy.biomedtrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.riramzy.biomedtrack.data.local.entity.DepartmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {
    @Query("SELECT * FROM department")
    fun getAllDepartments(): Flow<List<DepartmentEntity>>

    @Query("SELECT * FROM department")
    fun getAllDepartmentsOnce(): List<DepartmentEntity>

    @Query("SELECT * FROM department WHERE id = :id")
    suspend fun getDepartmentById(id: String): DepartmentEntity

    @Query("SELECT COUNT(*) FROM department")
    suspend fun getDepartmentsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: DepartmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDepartments(department: List<DepartmentEntity>)

    @Update
    suspend fun updateDepartment(department: DepartmentEntity)

    @Query("DELETE FROM department WHERE id = :id")
    suspend fun deleteDepartmentById(id: String)
}
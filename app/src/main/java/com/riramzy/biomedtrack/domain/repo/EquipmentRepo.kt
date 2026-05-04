package com.riramzy.biomedtrack.domain.repo

import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import kotlinx.coroutines.flow.Flow

interface EquipmentRepo {
    fun getAllEquipment(): Flow<List<Equipment>>
    suspend fun getAllEquipmentOnce(): List<Equipment>
    fun getEquipmentByDepartment(department: Department): Flow<List<Equipment>>
    suspend fun getEquipmentByDepartmentOnce(department: Department): List<Equipment>
    suspend fun getEquipmentById(id: String): Equipment?
    suspend fun getEquipmentCount(): Triple<Int, Int, Int>
    suspend fun addEquipment(equipment: Equipment): Result<Unit>
    suspend fun batchInsertEquipment(equipmentList: List<Equipment>): Result<Unit>
    suspend fun updateEquipment(equipment: Equipment): Result<Unit>
    suspend fun deleteEquipment(id: String): Result<Unit>
}


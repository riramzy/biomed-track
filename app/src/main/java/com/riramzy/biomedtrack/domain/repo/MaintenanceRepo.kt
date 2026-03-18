package com.riramzy.biomedtrack.domain.repo

import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import kotlinx.coroutines.flow.Flow
import com.riramzy.biomedtrack.domain.Result

interface MaintenanceRepo {
    fun getEquipmentLog(equipmentId: String): Flow<List<MaintenanceLog>>
    fun getLogsByTechnician(technicianId: String): Flow<List<MaintenanceLog>>
    suspend fun getLogsByDateRange(startDate: String, endDate: String, department: Department? = null): List<MaintenanceLog>
    suspend fun updateLog(log: MaintenanceLog): Result<Unit>
    suspend fun addLog(log: MaintenanceLog): Result<Unit>
    suspend fun deleteLog(id: String): Result<Unit>
}
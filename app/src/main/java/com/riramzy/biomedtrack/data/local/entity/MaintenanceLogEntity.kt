package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.utils.MaintenanceType

@Entity(tableName = "maintenance_log")
data class MaintenanceLogEntity(
    @PrimaryKey val id: String,
    val equipmentId: String,
    val equipmentName: String,
    val equipmentModel: String,
    val equipmentSerial: String,
    val department: Department,
    val type: MaintenanceType,
    val technicianId: String,
    val technicianName: String,
    val date: Long,
    val currentStatus: EquipmentStatus,
    val checklist: List<ChecklistItem>,
    val notes: String,
    val photoUrl: String? = null,
    val workDone: String,
    val readBy: List<String> = emptyList()
) {
    fun toDomain() = MaintenanceLog(
        id = id,
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        equipmentModel = equipmentModel,
        equipmentSerial = equipmentSerial,
        department = department,
        type = type,
        technicianId = technicianId,
        technicianName = technicianName,
        date = date,
        currentStatus = currentStatus,
        checklist = checklist,
        notes = notes,
        photoUrl = photoUrl,
        workDone = workDone,
        readBy = readBy
    )
}

fun MaintenanceLog.toEntity() = MaintenanceLogEntity(
    id = id,
    equipmentId = equipmentId,
    equipmentName = equipmentName,
    equipmentModel = equipmentModel,
    equipmentSerial = equipmentSerial,
    department = department,
    type = type,
    technicianId = technicianId,
    technicianName = technicianName,
    date = date,
    currentStatus = currentStatus,
    checklist = checklist,
    notes = notes,
    photoUrl = photoUrl,
    workDone = workDone,
    readBy = readBy
)
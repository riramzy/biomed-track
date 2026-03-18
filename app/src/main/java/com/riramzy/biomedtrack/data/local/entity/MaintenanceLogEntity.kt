package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.model.MaintenanceType
import kotlin.String

@Entity(tableName = "maintenance_log")
data class MaintenanceLogEntity(
    @PrimaryKey val id: String,
    val equipmentId: String,
    val equipmentName: String,
    val equipmentSerial: String,
    val department: String,
    val type: String,
    val technicianId: String,
    val technicianName: String,
    val date: String,
    val currentStatus: String,
    val checklist: String,
    val notes: String,
    val photoUrl: String? = null,
    val workDone: String
) {
    fun toDomain() = MaintenanceLog(
        id = id,
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        equipmentSerial = equipmentSerial,
        department = Department.valueOf(department),
        type = MaintenanceType.valueOf(type),
        technicianId = technicianId,
        technicianName = technicianName,
        date = date,
        currentStatus = EquipmentStatus.valueOf(currentStatus),
        checklist = Gson().fromJson(checklist, Array<ChecklistItem>::class.java).toList(),
        notes = notes,
        photoUrl = photoUrl,
        workDone = workDone
    )
}

fun MaintenanceLog.toEntity() = MaintenanceLogEntity(
    id = id,
    equipmentId = equipmentId,
    equipmentName = equipmentName,
    equipmentSerial = equipmentSerial,
    department = department.name,
    type = type.name,
    technicianId = technicianId,
    technicianName = technicianName,
    date = date,
    currentStatus = currentStatus.name,
    checklist = Gson().toJson(checklist),
    notes = notes,
    photoUrl = photoUrl,
    workDone = workDone
)
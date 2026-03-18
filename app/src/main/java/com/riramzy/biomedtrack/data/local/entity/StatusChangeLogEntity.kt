package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.StatusChangeLog

@Entity(tableName = "status_change_log")
data class StatusChangeLogEntity(
    @PrimaryKey val id: String,
    val equipmentId: String,
    val equipmentName: String,
    val department: String,
    val previousStatus: String,
    val newStatus: String,
    val changedBy: String,
    val changedByName: String,
    val timestamp: String,
    val notes: String?
) {
    fun toDomain() = StatusChangeLog(
        id = id,
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        department = Department.valueOf(department),
        previousStatus = EquipmentStatus.valueOf(previousStatus),
        newStatus = EquipmentStatus.valueOf(newStatus),
        changedBy = changedBy,
        changedByName = changedByName,
        timestamp = timestamp,
        notes = notes
    )
}

fun StatusChangeLog.toEntity() = StatusChangeLogEntity(
    id = id,
    equipmentId = equipmentId,
    equipmentName = equipmentName,
    department = department.name,
    previousStatus = previousStatus.name,
    newStatus = newStatus.name,
    changedBy = changedBy,
    changedByName = changedByName,
    timestamp = timestamp,
    notes = notes
)
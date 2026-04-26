package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.StatusChangeLog

@Entity(tableName = "status_change_log")
data class StatusChangeLogEntity(
    @PrimaryKey val id: String,
    val equipmentId: String,
    val equipmentName: String,
    val equipmentModel: String,
    val equipmentSerial: String,
    val department: Department,
    val previousStatus: EquipmentStatus,
    val newStatus: EquipmentStatus,
    val changedBy: String,
    val changedByName: String,
    val timestamp: Long,
    val notes: String?,
    val readBy: List<String> = emptyList()
) {
    fun toDomain() = StatusChangeLog(
        id = id,
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        equipmentModel = equipmentModel,
        equipmentSerial = equipmentSerial,
        department = department,
        previousStatus = previousStatus,
        newStatus = newStatus,
        changedBy = changedBy,
        changedByName = changedByName,
        timestamp = timestamp,
        notes = notes,
        readBy = readBy
    )
}

fun StatusChangeLog.toEntity() = StatusChangeLogEntity(
    id = id,
    equipmentId = equipmentId,
    equipmentName = equipmentName,
    equipmentModel = equipmentModel,
    equipmentSerial = equipmentSerial,
    department = department,
    previousStatus = previousStatus,
    newStatus = newStatus,
    changedBy = changedBy,
    changedByName = changedByName,
    timestamp = timestamp,
    notes = notes,
    readBy = readBy
)
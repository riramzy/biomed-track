package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.StatusChangeLog

@Keep
data class StatusChangeLogDto(
    val id: String = "",
    val equipmentId: String = "",
    val equipmentName: String = "",
    val department: String = "",
    val previousStatus: String = "",
    val newStatus: String = "",
    val changedBy: String = "",
    val changedByName: String = "",
    val timestamp: String = "",
    val notes: String? = null
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

fun StatusChangeLog.toDto() = StatusChangeLogDto(
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
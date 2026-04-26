package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.StatusChangeLog

@Keep
data class StatusChangeLogDto(
    val id: String = "",
    val equipmentId: String = "",
    val equipmentName: String = "",
    val equipmentModel: String = "",
    val equipmentSerial: String = "",
    val department: DepartmentDto = DepartmentDto(),
    val previousStatus: String = "",
    val newStatus: String = "",
    val changedBy: String = "",
    val changedByName: String = "",
    val timestamp: Long = 0L,
    val notes: String? = null,
    val readBy: List<String> = emptyList()
) {
    fun toDomain() = StatusChangeLog(
        id = id,
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        equipmentModel = equipmentModel,
        equipmentSerial = equipmentSerial,
        department = department.toDomain(),
        previousStatus = EquipmentStatus.valueOf(previousStatus),
        newStatus = EquipmentStatus.valueOf(newStatus),
        changedBy = changedBy,
        changedByName = changedByName,
        timestamp = timestamp,
        notes = notes,
        readBy = readBy
    )
}

fun StatusChangeLog.toDto() = StatusChangeLogDto(
    id = id,
    equipmentId = equipmentId,
    equipmentName = equipmentName,
    equipmentModel = equipmentModel,
    equipmentSerial = equipmentSerial,
    department = department.toDto(),
    previousStatus = previousStatus.name,
    newStatus = newStatus.name,
    changedBy = changedBy,
    changedByName = changedByName,
    timestamp = timestamp,
    notes = notes,
    readBy = readBy
)

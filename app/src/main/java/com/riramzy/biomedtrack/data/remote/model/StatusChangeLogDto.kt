package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.utils.EquipmentStatus

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
        previousStatus = try {
            EquipmentStatus.valueOf(previousStatus)
        } catch (e: Exception) {
            e.printStackTrace()
            EquipmentStatus.ONLINE
        },
        newStatus = try {
            EquipmentStatus.valueOf(newStatus)
        } catch (e: Exception) {
            e.printStackTrace()
            EquipmentStatus.ONLINE
        },
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

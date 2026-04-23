package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.utils.MaintenanceType

@Keep
data class MaintenanceLogDto(
    val id: String = "",
    val equipmentId: String = "",
    val equipmentName: String = "",
    val equipmentSerial: String = "",
    val department: DepartmentDto = DepartmentDto(),
    val type: String = "",
    val technicianId: String = "",
    val technicianName: String = "",
    val date: String = "",
    val currentStatus: String = "",
    val checklist: List<Map<String, Any>> = emptyList(),
    val notes: String = "",
    val photoUrl: String? = null,
    val workDone: String = ""
) {
    fun toDomain() = MaintenanceLog(
        id = id,
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        equipmentSerial = equipmentSerial,
        department = department.toDomain(),
        type = MaintenanceType.valueOf(type),
        technicianId = technicianId,
        technicianName = technicianName,
        date = date,
        currentStatus = EquipmentStatus.valueOf(currentStatus),
        checklist = checklist.map { map ->
            ChecklistItem(
                id = map["id"] as? String ?: "",
                label = map["label"] as? String ?: "",
                isChecked = map["isChecked"] as? Boolean ?: false
            )
        },
        notes = notes,
        photoUrl = photoUrl,
        workDone = workDone
    )
}

fun MaintenanceLog.toDto() = MaintenanceLogDto(
    id = id,
    equipmentId = equipmentId,
    equipmentName = equipmentName,
    equipmentSerial = equipmentSerial,
    department = department.toDto(),
    type = type.name,
    technicianId = technicianId,
    technicianName = technicianName,
    date = date,
    currentStatus = currentStatus.name,
    checklist = checklist.map { item ->
        mapOf("id" to item.id, "label" to item.label, "isChecked" to item.isChecked)
    },
    notes = notes,
    photoUrl = photoUrl,
    workDone = workDone
)
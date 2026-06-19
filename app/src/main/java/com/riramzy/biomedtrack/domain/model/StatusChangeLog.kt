package com.riramzy.biomedtrack.domain.model

import com.riramzy.biomedtrack.utils.EquipmentStatus

data class StatusChangeLog(
    val id: String,
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
)

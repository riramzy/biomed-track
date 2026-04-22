package com.riramzy.biomedtrack.domain.model

import com.riramzy.biomedtrack.utils.EquipmentStatus

// Records every time a machine's status changes. Powers the status history timeline on the Equipment Detail screen and triggers push notifications
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
    val timestamp: String,
    val notes: String?
)

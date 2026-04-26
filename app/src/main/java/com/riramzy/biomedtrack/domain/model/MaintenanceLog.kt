package com.riramzy.biomedtrack.domain.model

import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.MaintenanceType

// Represents a single maintenance entry logged by an engineer
data class MaintenanceLog(
    val id: String,
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
)

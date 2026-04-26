package com.riramzy.biomedtrack.domain.model

import com.riramzy.biomedtrack.utils.EquipmentStatus

// Represent a single piece of medical equipment
data class Equipment(
    val id: String,
    val name: String,
    val model: String,
    val serialNumber: String,
    val manufacturer: String,
    val agent: String,
    val category: String,
    val department: Department,
    val status: EquipmentStatus,
    val location: String,
    val installDate: Long,
    val lastMaintenanceDate: Long? = null,
    val nextMaintenanceDate: Long? = null,
    val serviceIntervalDays: Int,
    val contractInfo: String? = null,
    val warrantyEndDate: Long? = null,
    val createdBy: String,
    val assignedTo: String? = null,
)

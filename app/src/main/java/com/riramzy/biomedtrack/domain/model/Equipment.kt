package com.riramzy.biomedtrack.domain.model

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
    val installDate: String,
    val lastMaintenanceDate: String? = null,
    val nextMaintenanceDate: String? = null,
    val serviceIntervalDays: Int,
    val contractInfo: String? = null,
    val createdBy: String,
    val assignedTo: String? = null,
)

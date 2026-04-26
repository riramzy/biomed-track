package com.riramzy.biomedtrack.utils

data class ActivityItem(
    val id: String,
    val type: ActivityType,
    val title: String,
    val equipmentId: String,
    val equipmentName: String,
    val equipmentModel: String,
    val equipmentSerial: String,
    val departmentName: String,
    val technicianName: String,
    val timestamp: Long,
    val dueDate: String? = null,
    val status: String,
    val isRead: Boolean = false
)


package com.riramzy.biomedtrack.domain.model

import com.riramzy.biomedtrack.utils.TaskStatus

// Represents a scheduled maintenance task assigned by a Supervisor
data class Task(
    val id: String,
    val equipmentId: String,
    val equipmentName: String,
    val equipmentModel: String,
    val equipmentSerial: String,
    val department: Department,
    val assignedTo: String,
    val assignedToName: String,
    val assignedBy: String,
    val dueDate: Long,
    val status: TaskStatus,
    val notes: String,
    val scheduledChecklist: List<ChecklistItem>,
    val readBy: List<String> = emptyList()
)

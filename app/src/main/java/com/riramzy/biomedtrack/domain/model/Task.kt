package com.riramzy.biomedtrack.domain.model

// Represents a scheduled maintenance task assigned by a Supervisor
data class Task(
    val id: String,
    val equipmentId: String,
    val equipmentName: String,
    val department: Department,
    val assignedTo: String,
    val assignedToName: String,
    val assignedBy: String,
    val dueDate: String,
    val status: TaskStatus,
    val notes: String,
    val scheduledChecklist: List<ChecklistItem>
)

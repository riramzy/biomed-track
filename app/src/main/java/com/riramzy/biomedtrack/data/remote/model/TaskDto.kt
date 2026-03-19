package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.model.TaskStatus

@Keep
data class TaskDto(
    val id: String = "",
    val equipmentId: String = "",
    val equipmentName: String = "",
    val department: String = "",
    val assignedTo: String = "",
    val assignedToName: String = "",
    val assignedBy: String = "",
    val dueDate: String = "",
    val status: String = "",
    val notes: String = "",
    val scheduledChecklist: List<Map<String, Any>> = emptyList()
) {
    fun toDomain() = Task(
        id = id,
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        department = Department.valueOf(department),
        assignedTo = assignedTo,
        assignedToName = assignedToName,
        assignedBy = assignedBy,
        dueDate = dueDate,
        status = TaskStatus.valueOf(status),
        notes = notes,
        scheduledChecklist = scheduledChecklist.map {
            ChecklistItem(
                id = it["id"] as? String ?: "",
                label = it["label"] as? String ?: "",
                isChecked = it["isChecked"] as? Boolean ?: false
            )
        }
    )
}

fun Task.toDto() = TaskDto(
    id = id,
    equipmentId = equipmentId,
    equipmentName = equipmentName,
    department = department.name,
    assignedTo = assignedTo,
    assignedToName = assignedToName,
    assignedBy = assignedBy,
    dueDate = dueDate,
    status = status.name,
    notes = notes,
    scheduledChecklist = scheduledChecklist.map {
        mapOf("id" to it.id, "label" to it.label, "isChecked" to it.isChecked)
    }
)
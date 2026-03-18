package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.model.TaskStatus

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey val id: String,
    val equipmentId: String,
    val equipmentName: String,
    val department: String,
    val assignedTo: String,
    val assignedToName: String,
    val assignedBy: String,
    val dueDate: String,
    val status: String,
    val notes: String,
    val scheduledChecklist: String
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
        scheduledChecklist = Gson().fromJson(scheduledChecklist, Array<ChecklistItem>::class.java).toList()
    )
}

fun Task.toEntity() = TaskEntity(
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
    scheduledChecklist = Gson().toJson(scheduledChecklist)
)
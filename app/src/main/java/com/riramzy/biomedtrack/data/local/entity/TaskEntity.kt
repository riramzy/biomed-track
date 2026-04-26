package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.utils.TaskStatus

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey val id: String,
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
) {
    fun toDomain() = Task(
        id = id,
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        equipmentModel = equipmentModel,
        equipmentSerial = equipmentSerial,
        department = department,
        assignedTo = assignedTo,
        assignedToName = assignedToName,
        assignedBy = assignedBy,
        dueDate = dueDate,
        status = status,
        notes = notes,
        scheduledChecklist = scheduledChecklist,
        readBy = readBy
    )
}

fun Task.toEntity() = TaskEntity(
    id = id,
    equipmentId = equipmentId,
    equipmentName = equipmentName,
    equipmentModel = equipmentModel,
    equipmentSerial = equipmentSerial,
    department = department,
    assignedTo = assignedTo,
    assignedToName = assignedToName,
    assignedBy = assignedBy,
    dueDate = dueDate,
    status = status,
    notes = notes,
    scheduledChecklist = scheduledChecklist,
    readBy = readBy
)
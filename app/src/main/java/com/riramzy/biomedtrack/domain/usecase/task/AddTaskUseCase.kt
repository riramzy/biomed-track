package com.riramzy.biomedtrack.domain.usecase.task

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.Result
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import jakarta.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!sessionManager.hasPermission(Permission.ASSIGN_TASKS)) {
            return Result.Error("User doesn't have permission to assign tasks")
        }

        if (task.equipmentId.isBlank()) {
            return Result.Error("Equipment id cannot be empty")
        }

        if (task.assignedTo.isBlank()) {
            return Result.Error("Assigned to cannot be empty")
        }

        if (task.dueDate.isBlank()) {
            return Result.Error("Due date cannot be empty")
        }

        return taskRepository.addTask(task)
    }
}
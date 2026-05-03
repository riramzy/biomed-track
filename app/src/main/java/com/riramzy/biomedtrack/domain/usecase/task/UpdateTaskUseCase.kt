package com.riramzy.biomedtrack.domain.usecase.task

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import jakarta.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!sessionManager.hasPermission(Permission.ASSIGN_TASKS)) {
            return Result.Error("User doesn't have permission to assign tasks")
        }

        return taskRepository.updateTask(task)
    }
}
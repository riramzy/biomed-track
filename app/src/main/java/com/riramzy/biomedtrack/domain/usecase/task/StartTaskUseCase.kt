package com.riramzy.biomedtrack.domain.usecase.task

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.TaskStatus
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class StartTaskUseCase @Inject constructor(
    private val taskRepo: TaskRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(taskId: String): Result<Unit> {
        val user = sessionManager.currentUser.value
            ?: return Result.Error("User not logged in")

        val taskSnapshot = taskRepo.getTasksForTechnician(user.id).firstOrNull()

        val targetedTask = taskSnapshot?.find { it.id == taskId }
            ?: return Result.Error("Task cannot be found or not assigned to you")

        if (targetedTask.status == TaskStatus.PENDING) {
            return taskRepo.updateTask(targetedTask.copy(status = TaskStatus.IN_PROGRESS))
        }

        return Result.Success(Unit)
    }
}
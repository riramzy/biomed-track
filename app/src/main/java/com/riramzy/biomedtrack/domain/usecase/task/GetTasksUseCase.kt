package com.riramzy.biomedtrack.domain.usecase.task

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import com.riramzy.biomedtrack.utils.UserRole
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepo,
    private val sessionManager: SessionManager
) {
    operator fun invoke(): Flow<List<Task>> {
        val user = sessionManager.currentUser.value ?: return emptyFlow()

        return if (user.role == UserRole.ADMIN || user.role == UserRole.SUPERVISOR) {
            taskRepository.getAllTasks()
        } else {
            taskRepository.getTasksForTechnician(user.id)
        }
    }
}
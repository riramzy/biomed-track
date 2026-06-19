package com.riramzy.biomedtrack.domain.usecase.task

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import com.riramzy.biomedtrack.utils.UserRole
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepo,
    private val sessionManager: SessionManager
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Task>> {
        return sessionManager.currentUser.flatMapLatest { user ->
            if (user == null) {
                emptyFlow()
            } else if (user.role == UserRole.ADMIN || user.role == UserRole.SUPERVISOR) {
                taskRepository.getAllTasks()
            } else {
                taskRepository.getTasksForTechnician(user.id)
            }
        }
    }
}
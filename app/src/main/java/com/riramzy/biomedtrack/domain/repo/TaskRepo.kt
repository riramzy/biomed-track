package com.riramzy.biomedtrack.domain.repo

import com.riramzy.biomedtrack.domain.model.Task
import kotlinx.coroutines.flow.Flow
import com.riramzy.biomedtrack.utils.Result

interface TaskRepo {
    fun getTasksForTechnician(technicianId: String): Flow<List<Task>>
    fun getAllTasks(): Flow<List<Task>>
    suspend fun addTask(task: Task): Result<Unit>
    suspend fun updateTask(task: Task): Result<Unit>
    suspend fun deleteTask(taskId: String): Result<Unit>
    suspend fun markAsRead(logId: String, userId: String): Result<Unit>
}
package com.riramzy.biomedtrack.data.repo

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.riramzy.biomedtrack.data.local.dao.TaskDao
import com.riramzy.biomedtrack.data.local.entity.toEntity
import com.riramzy.biomedtrack.data.remote.firebase.FirestoreCollections
import com.riramzy.biomedtrack.data.remote.model.TaskDto
import com.riramzy.biomedtrack.data.remote.model.toDto
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.repo.TaskRepo
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val taskDao: TaskDao
): TaskRepo {
    override fun getAllTasks(): Flow<List<Task>> = callbackFlow {
        val listener = firebaseFirestore
            .collection(FirestoreCollections.TASKS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val tasks = snapshot?.documents?.mapNotNull {
                    it.toObject(TaskDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(tasks)

                CoroutineScope(Dispatchers.IO).launch {
                    for (task in tasks) {
                        taskDao.insertTask(task.toEntity())
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getTasksForTechnician(technicianId: String): Flow<List<Task>> = callbackFlow {
        val listener = firebaseFirestore
            .collection(FirestoreCollections.TASKS)
            .whereEqualTo("assignedTo", technicianId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val tasks = snapshot?.documents?.mapNotNull {
                    it.toObject(TaskDto::class.java)?.toDomain()
                }

                trySend(tasks ?: emptyList())

                CoroutineScope(Dispatchers.IO).launch {
                    for (task in tasks ?: emptyList()) {
                        taskDao.insertTask(task.toEntity())
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addTask(task: Task): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.TASKS)
                .document(task.id)
                .set(task.toDto())
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to add task", e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.TASKS)
                .document(task.id)
                .set(task.toDto())
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update task", e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.TASKS)
                .document(taskId)
                .delete()
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete task", e)
        }
    }

    override suspend fun markAsRead(logId: String, userId: String): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.STATUS_CHANGE_LOGS)
                .document(logId)
                .update("readBy", FieldValue.arrayUnion(userId))
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to mark as read", e)
        }
    }
}
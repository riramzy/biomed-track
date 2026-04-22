package com.riramzy.biomedtrack.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.riramzy.biomedtrack.data.local.dao.StatusChangeLogDao
import com.riramzy.biomedtrack.data.local.entity.toEntity
import com.riramzy.biomedtrack.data.remote.firebase.FirestoreCollections
import com.riramzy.biomedtrack.data.remote.model.StatusChangeLogDto
import com.riramzy.biomedtrack.data.remote.model.toDto
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.repo.StatusChangeRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StatusChangeRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val statusChangeDao: StatusChangeLogDao
): StatusChangeRepo {
    override fun getRecentStatusChanges(limit: Int): Flow<List<StatusChangeLog>> = callbackFlow {
        val listener = firebaseFirestore
            .collection(FirestoreCollections.STATUS_CHANGE_LOGS)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val statusChanges = snapshot?.documents?.mapNotNull {
                    it.toObject(StatusChangeLogDto::class.java)?.toDomain()
                }

                trySend(statusChanges ?: emptyList())

                CoroutineScope(Dispatchers.IO).launch {
                    for (statusChange in statusChanges ?: emptyList()) {
                        statusChangeDao.insertStatusChangeLog(statusChange.toEntity())
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getStatusChangeForEquipment(equipmentId: String): Flow<List<StatusChangeLog>> = callbackFlow {
        val listener = firebaseFirestore
            .collection(FirestoreCollections.STATUS_CHANGE_LOGS)
            .whereEqualTo("equipmentId", equipmentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val statusChanges = snapshot?.documents?.mapNotNull {
                    it.toObject(StatusChangeLogDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(statusChanges)

                CoroutineScope(Dispatchers.IO).launch {
                    for (statusChange in statusChanges) {
                        statusChangeDao.insertStatusChangeLog(statusChange.toEntity())
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun logStatusChange(statusChangeLog: StatusChangeLog): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.STATUS_CHANGE_LOGS)
                .document(statusChangeLog.id)
                .set(statusChangeLog.toDto())
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to log status change", e)
        }
    }
}
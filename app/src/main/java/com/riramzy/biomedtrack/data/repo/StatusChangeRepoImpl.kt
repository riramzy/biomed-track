package com.riramzy.biomedtrack.data.repo

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.riramzy.biomedtrack.data.local.dao.StatusChangeLogDao
import com.riramzy.biomedtrack.data.local.entity.toEntity
import com.riramzy.biomedtrack.data.remote.firebase.FirestoreCollections
import com.riramzy.biomedtrack.data.remote.model.StatusChangeLogDto
import com.riramzy.biomedtrack.data.remote.model.toDto
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.repo.StatusChangeRepo
import com.riramzy.biomedtrack.utils.FcmDispatcher
import com.riramzy.biomedtrack.utils.Result
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
    private val statusChangeDao: StatusChangeLogDao,
    private val dispatcher: FcmDispatcher
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

            try {
                val usersSnap = firebaseFirestore
                    .collection(FirestoreCollections.TECHNICIANS)
                    .get()
                    .await()

                for (doc in usersSnap.documents) {
                    val role = doc.getString("role")
                    val isSupervisorOrAdmin = role == "ADMIN" || role == "SUPERVISOR"

                    val depts = doc.get("assignedDepartments") as? List<*>

                    val isInDept = isSupervisorOrAdmin || depts?.any { dept ->
                        (dept as? Map<*, *>)?.get("id") == statusChangeLog.department.id
                    } == true

                    val token = doc.getString("fcmToken")

                    if (isInDept && !token.isNullOrEmpty()) {
                        dispatcher.pushNotification(
                            targetToken = token,
                            title = "Status changed from ${statusChangeLog.previousStatus} to ${statusChangeLog.newStatus}",
                            body = "${statusChangeLog.equipmentName} was updated by ${statusChangeLog.changedByName}",
                            equipmentId = statusChangeLog.equipmentId
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to log status change", e)
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
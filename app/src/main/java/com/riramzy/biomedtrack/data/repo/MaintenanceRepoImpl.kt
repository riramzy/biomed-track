package com.riramzy.biomedtrack.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.riramzy.biomedtrack.data.local.dao.MaintenanceLogDao
import com.riramzy.biomedtrack.data.local.entity.toEntity
import com.riramzy.biomedtrack.data.remote.firebase.FirestoreCollections
import com.riramzy.biomedtrack.data.remote.model.MaintenanceLogDto
import com.riramzy.biomedtrack.data.remote.model.toDto
import com.riramzy.biomedtrack.domain.Result
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MaintenanceRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val maintenanceDao: MaintenanceLogDao,
): MaintenanceRepo {
    override fun getEquipmentLog(equipmentId: String): Flow<List<MaintenanceLog>> = callbackFlow {
        val listener = firebaseFirestore
            .collection(FirestoreCollections.MAINTENANCE_LOGS)
            .whereEqualTo("equipmentId", equipmentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val logs = snapshot?.documents?.mapNotNull {
                    it.toObject(MaintenanceLogDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(logs)

                CoroutineScope(Dispatchers.IO).launch {
                    for (log in logs) {
                        maintenanceDao.insertMaintenanceLog(log.toEntity())
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getLogsByTechnician(technicianId: String): Flow<List<MaintenanceLog>> = callbackFlow {
        val listener = firebaseFirestore
            .collection(FirestoreCollections.MAINTENANCE_LOGS)
            .whereEqualTo("technicianId", technicianId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val logs = snapshot?.documents?.mapNotNull {
                    it.toObject(MaintenanceLogDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(logs)

                CoroutineScope(Dispatchers.IO).launch {
                    for (log in logs) {
                        maintenanceDao.insertMaintenanceLog(log.toEntity())
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getLogsByDateRange(
        startDate: String,
        endDate: String,
        department: Department?
    ): List<MaintenanceLog> {
        var query = firebaseFirestore
            .collection(FirestoreCollections.MAINTENANCE_LOGS)
            .whereGreaterThanOrEqualTo("date", startDate)
            .whereLessThanOrEqualTo("date", endDate)

        if (department != null) {
            query = query.whereEqualTo("department", department.name)
        }

        return query
            .get()
            .await()
            .mapNotNull { it.toObject(MaintenanceLogDto::class.java).toDomain() }
    }

    override suspend fun addLog(log: MaintenanceLog): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.MAINTENANCE_LOGS)
                .document(log.id)
                .set(log.toDto())
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to add maintenance log", e)
        }
    }

    override suspend fun updateLog(log: MaintenanceLog): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.MAINTENANCE_LOGS)
                .document(log.id)
                .set(log.toDto())
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update maintenance log", e)
        }
    }

    override suspend fun deleteLog(id: String): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.MAINTENANCE_LOGS)
                .document(id)
                .delete()
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete maintenance log", e)
        }
    }
}
package com.riramzy.biomedtrack.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.riramzy.biomedtrack.data.local.dao.EquipmentDao
import com.riramzy.biomedtrack.data.local.entity.toEntity
import com.riramzy.biomedtrack.data.remote.firebase.FirestoreCollections
import com.riramzy.biomedtrack.data.remote.model.EquipmentDto
import com.riramzy.biomedtrack.data.remote.model.toDto
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.Result
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EquipmentRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val equipmentDao: EquipmentDao,
    private val sessionManager: SessionManager,
): EquipmentRepo {
    override fun getAllEquipment(): Flow<List<Equipment>> = callbackFlow {
        // Create a listener for the equipment collection
        val listener = firebaseFirestore
            .collection(FirestoreCollections.EQUIPMENT)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val equipment = snapshot?.documents?.mapNotNull {
                    // Convert the document to an Equipment object
                    it.toObject(EquipmentDto::class.java)?.toDomain()
                } ?: emptyList()

                // Send the equipment to the flow
                trySend(equipment)

                // Update the local database
                CoroutineScope(Dispatchers.IO).launch {
                    equipmentDao.insertAllEquipment(equipment.map { it.toEntity() })
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getAllEquipmentOnce(): List<Equipment> {
        return firebaseFirestore
            .collection(FirestoreCollections.EQUIPMENT)
            .get()
            .await()
            .mapNotNull { it.toObject(EquipmentDto::class.java).toDomain() }
    }

    override suspend fun getEquipmentCount(): Triple<Int, Int, Int> {
        return Triple(
            equipmentDao.getDownEquipmentCount(),
            equipmentDao.getOnlineEquipmentCount(),
            equipmentDao.getDueServiceEquipmentCount()
        )
    }

    override fun getEquipmentByDepartment(department: Department): Flow<List<Equipment>> {
        return getAllEquipment().map { equipment ->
            equipment.filter { it.department == department }
        }
    }

    override suspend fun getEquipmentByDepartmentOnce(department: Department): List<Equipment> {
        return getAllEquipmentOnce().filter { equipment ->
            equipment.department == department
        }
    }

    override suspend fun getEquipmentById(id: String): Equipment? {
        return equipmentDao.getEquipmentById(id)?.toDomain()
            ?: firebaseFirestore
                .collection(FirestoreCollections.EQUIPMENT)
                .document(id)
                .get()
                .await()
                .toObject(EquipmentDto::class.java)?.toDomain()
    }

    override suspend fun addEquipment(equipment: Equipment): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.EQUIPMENT)
                .document(equipment.id)
                .set(equipment.toDto())
                .await()
            equipmentDao.insertEquipment(equipment.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to add equipment", e)
        }
    }

    override suspend fun updateEquipment(equipment: Equipment): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.EQUIPMENT)
                .document(equipment.id)
                .set(equipment.toDto())
                .await()
            equipmentDao.updateEquipment(equipment.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update equipment", e)
        }
    }

    override suspend fun deleteEquipment(id: String): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.EQUIPMENT)
                .document(id)
                .delete()
                .await()
            equipmentDao.deleteEquipmentById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete equipment", e)
        }
    }
}
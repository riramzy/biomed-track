package com.riramzy.biomedtrack.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.riramzy.biomedtrack.data.local.dao.DepartmentDao
import com.riramzy.biomedtrack.data.local.entity.toEntity
import com.riramzy.biomedtrack.data.remote.firebase.FirestoreCollections
import com.riramzy.biomedtrack.data.remote.model.DepartmentDto
import com.riramzy.biomedtrack.data.remote.model.toDto
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DepartmentRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val departmentDao: DepartmentDao,
    private val sessionManager: SessionManager,
): DepartmentRepo {
    override fun getAllDepartments(): Flow<List<Department>> = callbackFlow {
        val listener = firebaseFirestore
            .collection(FirestoreCollections.DEPARTMENT)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val department = snapshot?.documents?.mapNotNull {
                    // Convert the document to an Equipment object
                    it.toObject(DepartmentDto::class.java)?.toDomain()
                } ?: emptyList()

                // Send the equipment to the flow
                trySend(department)

                // Update the local database
                CoroutineScope(Dispatchers.IO).launch {
                    departmentDao.insertAllDepartments(department.map { it.toEntity() })
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getAllDepartmentsOnce(): List<Department> {
        return firebaseFirestore
            .collection(FirestoreCollections.EQUIPMENT)
            .get()
            .await()
            .mapNotNull { it.toObject(DepartmentDto::class.java).toDomain() }
    }

    override suspend fun getDepartmentById(id: String): Department? {
        return departmentDao.getDepartmentById(id)?.toDomain()
            ?: firebaseFirestore
                .collection(FirestoreCollections.DEPARTMENT)
                .document(id)
                .get()
                .await()
                .toObject(DepartmentDto::class.java)?.toDomain()
    }

    override suspend fun getDepartmentsCount(): Int {
        return departmentDao.getDepartmentsCount()
    }

    override suspend fun addDepartment(department: Department): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.EQUIPMENT)
                .document(department.id)
                .set(department.toDto())
                .await()
            departmentDao.insertDepartment(department.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to add department", e)
        }
    }

    override suspend fun updateDepartment(department: Department): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.DEPARTMENT)
                .document(department.id)
                .set(department.toDto())
                .await()
            departmentDao.updateDepartment(department.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update department", e)
        }
    }

    override suspend fun deleteDepartment(id: String): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.DEPARTMENT)
                .document(id)
                .delete()
                .await()
            departmentDao.deleteDepartmentById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete department", e)
        }
    }
}
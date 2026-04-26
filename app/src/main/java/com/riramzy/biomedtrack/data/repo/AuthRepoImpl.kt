package com.riramzy.biomedtrack.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.riramzy.biomedtrack.data.local.dao.TechnicianDao
import com.riramzy.biomedtrack.data.local.entity.toEntity
import com.riramzy.biomedtrack.data.remote.firebase.FirestoreCollections
import com.riramzy.biomedtrack.data.remote.model.TechnicianDto
import com.riramzy.biomedtrack.data.remote.model.toDto
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val technicianDao: TechnicianDao,
): AuthRepo {
    override suspend fun createUser(technician: Technician, password: String): Result<Technician> {
        return try {
            firebaseAuth
                .createUserWithEmailAndPassword(technician.email, password)
                .await()

            val userId = firebaseAuth.currentUser?.uid ?: return Result.Error("Failed to create user")

            val updatedTechnician = technician.copy(id = userId)

            firebaseFirestore
                .collection(FirestoreCollections.TECHNICIANS)
                .document(userId)
                .set(updatedTechnician.toDto())
                .await()

            technicianDao.insertTechnician(updatedTechnician.toEntity())


            Result.Success(updatedTechnician)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to create user", e)
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Technician> {
        return try {
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()

            val userId = firebaseAuth.currentUser?.uid ?: return Result.Error("Failed to login")

            val technician = firebaseFirestore
                .collection(FirestoreCollections.TECHNICIANS)
                .document(userId)
                .get()
                .await()
                .toObject(TechnicianDto::class.java)?.toDomain() ?: return Result.Error("Failed to login")

            if (!technician.isActive) {
                return Result.Error("Account is deactivated")
            }

            Result.Success(technician)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to login", e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to logout", e)
        }
    }

    override suspend fun getCurrentUser(): Technician? {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return null

            firebaseFirestore
                .collection(FirestoreCollections.TECHNICIANS)
                .document(userId)
                .get()
                .await()
                .toObject(TechnicianDto::class.java)?.toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun getAllUsers(): Flow<List<Technician>> = callbackFlow {
        val listener = firebaseFirestore
            .collection(FirestoreCollections.TECHNICIANS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull {
                    it.toObject(TechnicianDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(users)

                CoroutineScope(Dispatchers.IO).launch {
                    for (user in users) {
                        technicianDao.insertTechnician(user.toEntity())
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun updateUserRole(
        userId: String,
        role: UserRole
    ): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.TECHNICIANS)
                .document(userId)
                .update("role", role.name)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update user role", e)
        }
    }

    override suspend fun updateUserDepartments(
        userId: String,
        departments: List<Department>
    ): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.TECHNICIANS)
                .document(userId)
                .update("assignedDepartments", departments.map { it.toDto() })
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update user's assigned departments", e)
        }
    }

    override suspend fun deactivateUser(userId: String): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.TECHNICIANS)
                .document(userId)
                .update("isActive", false)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to deactivate user", e)
        }
    }

    override suspend fun activateUser(userId: String): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(FirestoreCollections.TECHNICIANS)
                .document(userId)
                .update("isActive", true)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to activate user", e)
        }
    }

    override suspend fun updateFcmToken(
        userId: String,
        token: String
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseFirestore
                    .collection(FirestoreCollections.TECHNICIANS)
                    .document(userId)
                    .update("fcmToken", token)
                    .await()
                return@withContext Result.Success(Unit)
            } catch (e: Exception) {
               return@withContext Result.Error(e.message ?: "Failed to update fcm token", e)
            }
        }
    }
}
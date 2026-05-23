package com.riramzy.biomedtrack.data.repo

import android.content.Context
import com.google.firebase.FirebaseApp
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val technicianDao: TechnicianDao,
    @param:ApplicationContext private val context: Context
): AuthRepo {
    override suspend fun createUser(technician: Technician, password: String): Result<Technician> {
        return withContext(Dispatchers.IO) {
            try {
                // Use the Firebase Auth REST API to create the account without affecting
                // the current admin session. The SDK's createUserWithEmailAndPassword always
                // auto-signs-in the new user and corrupts the active session — the REST API
                // bypasses this entirely by operating as a pure server-side call.
                val apiKey = FirebaseApp.getInstance().options.apiKey
                val url = URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=$apiKey")

                val requestBody = JSONObject().apply {
                    put("email", technician.email)
                    put("password", password)
                    put("returnSecureToken", false)
                }.toString()

                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                connection.outputStream.use { it.write(requestBody.toByteArray()) }

                val responseCode = connection.responseCode
                val responseBody = if (responseCode == 200) {
                    connection.inputStream.bufferedReader().readText()
                } else {
                    connection.errorStream.bufferedReader().readText()
                }
                connection.disconnect()

                if (responseCode != 200) {
                    val errorMessage = JSONObject(responseBody)
                        .optJSONObject("error")
                        ?.optString("message") ?: "Failed to create user"
                    return@withContext Result.Error(errorMessage)
                }

                val userId = JSONObject(responseBody).getString("localId")
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
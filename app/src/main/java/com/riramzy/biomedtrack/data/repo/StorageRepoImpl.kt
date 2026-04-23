package com.riramzy.biomedtrack.data.repo

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.riramzy.biomedtrack.domain.repo.StorageRepo
import com.riramzy.biomedtrack.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepoImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
): StorageRepo {
    override suspend fun uploadFile(
        path: String,
        uri: Uri
    ): Result<String> {
        return try {
            val storageRef = firebaseStorage.reference
            val fileRef = storageRef.child(path)
            val uploadTask = fileRef.putFile(uri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to upload file", e)
        }
    }
}
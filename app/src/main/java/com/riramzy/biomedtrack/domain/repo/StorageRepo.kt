package com.riramzy.biomedtrack.domain.repo

import android.net.Uri
import com.riramzy.biomedtrack.utils.Result

interface StorageRepo {
    suspend fun uploadFile(path: String, uri: Uri): Result<String>
}
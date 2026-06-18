package com.riramzy.biomedtrack.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.OutputStream

data class ExportTarget(
    val uri: Uri,
    val outputStream: OutputStream
)
object FileExportHelper {
    fun getOutputStreamForExport(
        context: Context,
        fileName: String,
        mimeType: String
    ): ExportTarget? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            return uri?.let { u ->
                resolver.openOutputStream(u)?.let { stream ->
                    ExportTarget(u, stream)
                }
            }
        } else {
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!directory.exists()) directory.mkdirs()
            val file = java.io.File(directory, fileName)
            val stream = java.io.FileOutputStream(file)
            val uri = Uri.fromFile(file)
            return ExportTarget(uri, stream)
        }
    }
}
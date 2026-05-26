package com.riramzy.biomedtrack.utils

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import com.riramzy.biomedtrack.R
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class FcmDispatcher(
    private val context: Context
) {
    private val client = HttpClient(CIO)
    private val projectId = "biomed-track"

    suspend fun pushNotification(
        targetToken: String,
        title: String,
        body: String,
        equipmentId: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val stream = context.assets.open("fcm_service_account.json")
                val credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

                credentials.refreshIfExpired()

                val accessToken = credentials.accessToken.tokenValue
                val json = JSONObject().apply {
                    put("message", JSONObject().apply {
                        put("token", targetToken)
                        put("notification", JSONObject().apply {
                            put("title", title)
                            put("body", body)
                        })
                        put("data", JSONObject().apply {
                            put("equipmentId", equipmentId)
                        })
                    })
                }

                client.post(
                    "https://fcm.googleapis.com/v1/projects/$projectId/messages:send"
                ) {
                    contentType(ContentType.Application.Json)

                    headers {
                        append("Authorization", "Bearer $accessToken")
                    }

                    setBody(json.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
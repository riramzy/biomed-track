package com.riramzy.biomedtrack.data.remote.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.riramzy.biomedtrack.MainActivity
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class BioMedMessagingService: FirebaseMessagingService() {
    @Inject lateinit var sessionManager: SessionManager
    @Inject lateinit var authRepo: AuthRepo

    override fun onNewToken(token: String) {
        val user = sessionManager.currentUser.value
        if (user != null) {
            CoroutineScope(Dispatchers.IO).launch {
                authRepo.updateFcmToken(user.id, token)
            }
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title
        val body = message.notification?.body
        val data = message.data
        val equipmentId = data["equipmentId"]

        showNotification(title, body, equipmentId)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun showNotification(title: String?, body: String?, equipmentId: String?) {
        val channelId = "biomed_alerts"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Critical Equipment Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for status changes and assigned tasks"
            }

            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(
            this,
            MainActivity::class.java
        ).apply {
            putExtra("equipmentId", equipmentId)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.biomedtrack)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}
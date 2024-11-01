package com.m4ykey.valfi2.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.m4ykey.valfi2.MainActivity

class FCMService : FirebaseMessagingService() {

    private val channelId = "NEW_RELEASE_CHANNEL"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseToken", "Firebase Message Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
       super.onMessageReceived(message)

        val channel = NotificationChannel(
            channelId,
            "New Release",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Channel for new releases" }

        val notificationManager : NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("openFragment", "AlbumNewReleaseFragment")
        }
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(com.m4ykey.core.R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@FCMService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, notificationBuilder.build())
        }
    }

}
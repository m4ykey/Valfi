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
import com.m4ykey.valfi2.Utils.ALBUM_NEW_RELEASE_FRAGMENT
import com.m4ykey.valfi2.Utils.OPEN_FRAGMENT
import com.m4ykey.valfi2.Utils.CHANNEL_ID

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseToken", "Firebase Message Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
       super.onMessageReceived(message)

        createNotificationChannel()

        val (title, body) = getNotificationTitleAndBody(message)

        val pendingIntent = createPendingIntent()

        sendNotification(title, body, pendingIntent)
    }

    private fun sendNotification(title : String?, body : String?, pendingIntent: PendingIntent) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(com.m4ykey.core.R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (ActivityCompat.checkSelfPermission(
                this@FCMService,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(this)) {
                notify(1, notificationBuilder.build())
            }
        }
    }

    private fun createPendingIntent() : PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(OPEN_FRAGMENT, ALBUM_NEW_RELEASE_FRAGMENT)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getNotificationTitleAndBody(message : RemoteMessage) : Pair<String?, String?> {
        val title = message.notification?.title
        val body = message.notification?.body
        return title to (body ?: message.data["body"])
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "New Release",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Channel for new releases" }

        val notificationManager : NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
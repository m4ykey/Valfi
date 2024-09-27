package com.m4ykey.data.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.m4ykey.data.R

private val channelId = "NEW_RELEASE_CHANNEL"

fun sendNotification(context: Context) {
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(com.m4ykey.core.R.drawable.logo)
        .setContentTitle(context.getString(R.string.discover_new_releases))
        .setContentText(context.getString(R.string.new_albums_released))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(1, notificationBuilder.build())
    }
}

fun createNotificationChannel(context : Context) {
    val name = "New Release"
    val descriptionText = "Channel for new releases"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(
        channelId,
        name,
        importance
    ).apply { description = descriptionText }

    val notificationManager : NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
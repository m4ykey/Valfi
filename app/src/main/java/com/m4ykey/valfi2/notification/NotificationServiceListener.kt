package com.m4ykey.valfi2.notification

import android.content.pm.ApplicationInfo
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationServiceListener : NotificationListenerService() {

    companion object {
        private const val TAG = "MusicListener"
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d(TAG, "Removed Notification: ${sbn?.notification}")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        val extras = sbn?.notification?.extras
        val keys = extras?.keySet()

        val title = extras?.getCharSequence("android.title", "")?.toString()
        val artist = extras?.getCharSequence("android.text", "")?.toString()
        val applicationInfo = extras?.getParcelable("android.appInfo") as ApplicationInfo?
        val appInfo = applicationInfo?.packageName

        val songInfo = listOfNotNull(title, artist, appInfo).joinToString(" - ")

        if (artist != null) {
            MusicNotificationState.updateArtist(artist)
        }
        if (title != null) {
            MusicNotificationState.updateTitle(title)
        }

        if (keys != null) {
            for (key in keys) {
                val value = extras.get(key)
                Log.d(TAG, "Key: $key, Value: $value")
            }

            Log.d(TAG, "Received notification: $songInfo")
        }
    }

}
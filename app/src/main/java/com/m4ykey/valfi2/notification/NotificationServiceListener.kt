package com.m4ykey.valfi2.notification

import android.app.Notification
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
        val packageName = sbn?.packageName

        if (packageName?.let { isMusicApp(it) } == true) {
            val title = extras?.getCharSequence(Notification.EXTRA_TITLE, "")?.toString()
            val artist = extras?.getCharSequence(Notification.EXTRA_TEXT, "")?.toString()

            if (artist != null) {
                MusicNotificationState.updateArtist(artist)
            }
            if (title != null) {
                MusicNotificationState.updateTitle(title)
            }
        }
    }

    private fun isMusicApp(packageName : String) : Boolean {
        return packageName in listOf(
            "com.spotify.music"
        )
    }
}
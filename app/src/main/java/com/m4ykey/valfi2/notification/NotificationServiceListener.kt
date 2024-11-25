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
            val category = sbn.notification.category
            val title = extras?.getCharSequence(Notification.EXTRA_TITLE, "")?.toString()
            val artist = extras?.getCharSequence(Notification.EXTRA_TEXT, "")?.toString()

            if (category == Notification.CATEGORY_TRANSPORT
                && !artist.isNullOrEmpty() && !title.isNullOrEmpty()) {
                MusicNotificationState.updateTitle(title)
                MusicNotificationState.updateArtist(artist)
            } else {
                Log.i(TAG, "Notification does not contain music data.")
            }
        }
    }

    private fun isMusicApp(packageName : String) : Boolean {
        return packageName in listOf(
            "com.spotify.music"
        )
    }
}
package com.m4ykey.valfi2.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.m4ykey.valfi2.R
import com.m4ykey.valfi2.Utils.APPLE_MUSIC_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.DEEZER_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.PANDORA_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.SOUNDCLOUD_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.SPOTIFY_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.TIDAL_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.YOUTUBE_MUSIC_PACKAGE_NAME

class NotificationServiceListener : NotificationListenerService() {

    companion object {
        private const val TAG = "MusicListener"
        var currentMusicAppPackage : String? = null
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
            currentMusicAppPackage = packageName
            val category = sbn.notification.category
            val title = extras?.getCharSequence(Notification.EXTRA_TITLE, "")?.toString()
            val artist = extras?.getCharSequence(Notification.EXTRA_TEXT, "")?.toString()

            val appBackgroundRes = appBackground[packageName] ?: R.color.white
            val appStrokeColor = appStrokeColor[packageName] ?: R.color.gray

            if (category == Notification.CATEGORY_TRANSPORT
                && !artist.isNullOrEmpty() && !title.isNullOrEmpty()) {
                MusicNotificationState.updateTitle(title)
                MusicNotificationState.updateArtist(artist)
                MusicNotificationState.updateBackgroundColor(appBackgroundRes)
                MusicNotificationState.updateStrokeColor(appStrokeColor)
            } else {
                Log.i(TAG, "Notification does not contain music data.")
            }
        }
    }

    private fun isMusicApp(packageName : String) : Boolean {
        return packageName in listOf(
            SPOTIFY_PACKAGE_NAME,
            APPLE_MUSIC_PACKAGE_NAME,
            YOUTUBE_MUSIC_PACKAGE_NAME,
            DEEZER_PACKAGE_NAME ,
            TIDAL_PACKAGE_NAME,
            SOUNDCLOUD_PACKAGE_NAME,
            PANDORA_PACKAGE_NAME
        )
    }
}
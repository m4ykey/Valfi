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

        if (sbn == null) return

        val packageName = sbn.packageName
        val extras = sbn.notification?.extras
        val category = sbn.notification?.category

        if (!isMusicApp(packageName)) return

        val title = extras?.getCharSequence(Notification.EXTRA_TITLE)?.toString()?.trim()
        val artist = extras?.getCharSequence(Notification.EXTRA_TEXT)?.toString()?.trim()

        if (!title.isNullOrEmpty() && !artist.isNullOrEmpty() && category == Notification.CATEGORY_TRANSPORT) {
            currentMusicAppPackage = packageName

            val appBackgroundRes = appBackground[packageName] ?: R.color.white
            val appStrokeColor = appStrokeColor[packageName] ?: R.color.gray

            MusicNotificationState.updateTitle(title)
            MusicNotificationState.updateArtist(artist)
            MusicNotificationState.updateBackgroundColor(appBackgroundRes)
            MusicNotificationState.updateStrokeColor(appStrokeColor)
        }
    }

    private fun isMusicApp(packageName : String?) : Boolean {
        if (packageName == null) return false
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
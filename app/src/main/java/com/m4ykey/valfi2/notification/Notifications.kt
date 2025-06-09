package com.m4ykey.valfi2.notification

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.m4ykey.core.views.utils.showToast

private fun openNotificationAccessSettings(context: Context) {
    try {
        context.startActivity(
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    } catch (_ : ActivityNotFoundException) {
        showToast(context, "")
    }
}

private fun isNotificationListenerEnabled(context: Context) : Boolean {
    val pkgName = context.packageName
    val enabledListeners = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    return enabledListeners?.contains(pkgName) == true
}

fun checkNotificationListenerPermission(context: Context) : Boolean {
    return if (!isNotificationListenerEnabled(context)) {
        openNotificationAccessSettings(context)
        false
    } else {
        true
    }
}
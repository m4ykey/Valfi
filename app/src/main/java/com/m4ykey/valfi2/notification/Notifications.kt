package com.m4ykey.valfi2.notification

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private fun openNotificationAccessSettings(context: Context) {
    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
}

private fun isNotificationListenerEnabled(context: Context) : Boolean {
    val flat = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    return flat?.contains(context.packageName) == true
}

fun checkNotificationListenerPermission(context: Context) : Boolean {
    val permission = Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE

    if (!isNotificationListenerEnabled(context)) {
        openNotificationAccessSettings(context)
        return false
    } else {
        val isPermissionGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (!isPermissionGranted) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(permission),
                1
            )
            return false
        }
    }
    return true
}
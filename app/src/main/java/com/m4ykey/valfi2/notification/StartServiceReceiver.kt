package com.m4ykey.valfi2.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.m4ykey.valfi2.Utils.CUSTOM_START_SERVICE_ACTION

class StartServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == CUSTOM_START_SERVICE_ACTION) {
            val serviceIntent = Intent(context, NotificationServiceListener::class.java)
            context?.startService(serviceIntent)
        }
    }
}
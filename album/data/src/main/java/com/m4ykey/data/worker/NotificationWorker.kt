package com.m4ykey.data.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.m4ykey.data.notification.sendNotification

class NotificationWorker(
    context : Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        sendNotification(applicationContext)
        return Result.success()
    }
}
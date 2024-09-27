package com.m4ykey.valfi2

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.m4ykey.data.worker.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        scheduleNotificationWorker()
    }

    private fun scheduleNotificationWorker() {
        val initialDelay = calculateInitialDelayToFriday()

        val notificationRequest = PeriodicWorkRequestBuilder<NotificationWorker>(7, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "NewReleaseWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                notificationRequest
            )
    }

    private fun calculateInitialDelayToFriday() : Long {
        val calendar = Calendar.getInstance()

        val daysUntilNextFriday = (Calendar.FRIDAY - calendar.get(Calendar.DAY_OF_WEEK) + 7) % 7
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextFriday)

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val now = Calendar.getInstance().timeInMillis
        val targetTime = calendar.timeInMillis

        return targetTime - now
    }
}
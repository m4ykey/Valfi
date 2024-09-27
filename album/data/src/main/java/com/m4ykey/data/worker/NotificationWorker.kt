package com.m4ykey.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.notification.sendNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

class NotificationWorker(
    context : Context,
    workerParameters: WorkerParameters,
    private val repository : AlbumRepository
) : CoroutineWorker(context, workerParameters) {

    private var previousReleases : List<AlbumItem>? = null

    override suspend fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val hourOfWeek = calendar.get(Calendar.HOUR_OF_DAY)

        if (dayOfWeek == Calendar.FRIDAY && hourOfWeek == 12) {
            val newReleases = fetchNewReleases()

            if (isThereNewReleases(newReleases)) {
                sendNotification(applicationContext)
            }
        }
        return Result.success()
    }

    private fun isThereNewReleases(albums : List<AlbumItem>) : Boolean {
        return if (previousReleases == null) {
            previousReleases = albums
            true
        } else {
            val hasNewReleases = albums != previousReleases
            previousReleases = albums
            hasNewReleases
        }
    }

    private suspend fun fetchNewReleases() : List<AlbumItem> {
        return repository.getNewReleases(0, 20)
            .firstOrNull() ?: emptyList()
    }
}
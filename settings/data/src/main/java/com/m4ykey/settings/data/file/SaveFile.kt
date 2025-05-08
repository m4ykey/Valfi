package com.m4ykey.settings.data.file

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.GsonBuilder
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import java.io.IOException

suspend fun saveJsonToFile(context: Context, uri : Uri, jsonData : String) {
    withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(uri)?.sink()?.buffer()?.use { sink ->
                sink.writeUtf8(jsonData)
            } ?: throw IOException("Failed to create save file.")
        } catch (e : Exception) {
            Log.i("saveJsonToFile", "Error writing JSON to file", e)
        }
    }
}

suspend fun generateJsonData(
    repository: AlbumRepository,
    trackRepository: TrackRepository
) : String = withContext(Dispatchers.IO) {
    val savedAlbums = repository.getSavedAlbums().map { album ->
        album.copy(saveTime = album.saveTime)
    }
    val listenLaterAlbums = repository.getListenLaterAlbums().map { album ->
        album.copy(saveTime = album.saveTime)
    }

    val allAlbums = (savedAlbums + listenLaterAlbums).distinctBy { it.id }

    val tracksAlbum = allAlbums.flatMap { album ->
        trackRepository.getTracksById(album.id)
    }

    val albumsData = AlbumsData(
        savedAlbums = savedAlbums,
        listenLaterAlbums = listenLaterAlbums,
        trackEntity = tracksAlbum
    )

    GsonBuilder().setPrettyPrinting().create().toJson(albumsData)
}

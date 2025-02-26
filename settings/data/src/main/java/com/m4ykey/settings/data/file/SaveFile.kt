package com.m4ykey.settings.data.file

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.GsonBuilder
import com.m4ykey.data.domain.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

suspend fun saveJsonToFile(context: Context, uri : Uri, jsonData : String) {
    withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonData.toByteArray())
            } ?: throw IOException("Failed to create save file.")
        } catch (e : Exception) {
            Log.i("saveJsonToFile", "Error writing JSON to file", e)
        }
    }
}

suspend fun generateJsonData(repository: AlbumRepository) : String {
    val savedAlbums = repository.getSavedAlbums().map { album ->
        album.copy(saveTime = album.saveTime)
    }
    val listenLaterAlbums = repository.getListenLaterAlbums().map { album ->
        album.copy(saveTime = album.saveTime)
    }

    val albumsData = AlbumsData(
        savedAlbums = savedAlbums,
        listenLaterAlbums = listenLaterAlbums
    )

    return GsonBuilder().setPrettyPrinting().create().toJson(albumsData)
}

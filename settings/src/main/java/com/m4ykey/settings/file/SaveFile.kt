package com.m4ykey.settings.file

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.m4ykey.data.local.dao.AlbumDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun saveJsonToFile(context: Context, uri : Uri, jsonData : String) {
    withContext(Dispatchers.IO) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(jsonData.toByteArray())
            outputStream.flush()
        }
    }
}

suspend fun generateJsonData(albumDao: AlbumDao) : String {
    val savedAlbums = albumDao.getSavedAlbums()
    val listenLaterAlbums = albumDao.getListenLaterAlbums()

    val albumsData = AlbumsData(
        savedAlbums = savedAlbums,
        listenLaterAlbums = listenLaterAlbums
    )

    val gson = Gson()
    return gson.toJson(albumsData)
}

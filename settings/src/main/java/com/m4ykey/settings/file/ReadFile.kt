package com.m4ykey.settings.file

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun readJsonData(context: Context, uri: Uri): AlbumsData? {
    return withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val gson = Gson()

                val albumDataType = object : TypeToken<Map<String, Any>>() {}.type
                val jsonData: Map<String, Any> = gson.fromJson(reader, albumDataType)

                val savedAlbums = (jsonData["savedAlbums"] as? List<Map<String, Any>>)?.map {
                    convertMapToAlbumEntity(it)
                } ?: emptyList()

                val listenLaterAlbums = (jsonData["listenLaterAlbums"] as? List<Map<String, Any>>)?.map {
                    convertMapToAlbumEntity(it)
                } ?: emptyList()

                val savedAlbumsFlow = flow { emit(savedAlbums) }
                val listenLaterAlbumsFlow = flow { emit(listenLaterAlbums) }

                return@withContext AlbumsData(savedAlbumsFlow, listenLaterAlbumsFlow)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}

suspend fun insertAlbumData(albumDao: AlbumDao, albumsData: AlbumsData) {
    withContext(Dispatchers.IO) {
        try {
            albumsData.savedAlbums.collect { savedAlbums ->
                savedAlbums.forEach { album ->
                    albumDao.insertAlbum(album)
                    albumDao.insertSavedAlbum(IsAlbumSaved(album.id, true))
                }
            }

            albumsData.listenLaterAlbums.collect { listenLater ->
                listenLater.forEach { album ->
                    albumDao.insertAlbum(album)
                    albumDao.insertListenLaterAlbum(IsListenLaterSaved(album.id, true))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
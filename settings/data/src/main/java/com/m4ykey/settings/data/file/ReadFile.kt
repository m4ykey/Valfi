package com.m4ykey.settings.data.file

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.TrackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun readJsonData(context: Context, uri: Uri): AlbumsData? {
    return withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val gson = Gson()

                val albumDataType = object : TypeToken<Map<String, JsonElement>>() {}.type
                val jsonData: Map<String, JsonElement> = gson.fromJson(reader, albumDataType)

                val savedAlbums = jsonData["savedAlbums"]?.let { element ->
                    val albumListType = object : TypeToken<List<AlbumEntity>>() {}.type
                    gson.fromJson<List<AlbumEntity>>(element, albumListType)
                } ?: emptyList()

                val listenLaterAlbums = jsonData["listenLaterAlbums"]?.let { element ->
                    val albumListType = object : TypeToken<List<AlbumEntity>>() {}.type
                    gson.fromJson<List<AlbumEntity>>(element, albumListType)
                } ?: emptyList()

                val tracksAlbum = jsonData["tracksAlbum"]?.let { element ->
                    val trackListType = object : TypeToken<List<TrackEntity>>() {}.type
                    gson.fromJson<List<TrackEntity>>(element, trackListType)
                } ?: emptyList()

                val albumData = AlbumsData(savedAlbums, listenLaterAlbums, tracksAlbum)

                return@withContext albumData
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}

suspend fun insertAlbumData(
    repository: AlbumRepository,
    albumsData: AlbumsData,
    trackRepository: TrackRepository
) {
    withContext(Dispatchers.IO) {
        try {
            albumsData.savedAlbums.forEach { album ->
                val existingAlbum = repository.getAlbum(album.id)
                val albumToInsert = album.copy(
                    saveTime = existingAlbum?.saveTime ?: System.currentTimeMillis()
                )
                repository.insertAlbum(albumToInsert)
                repository.insertSavedAlbum(IsAlbumSaved(album.id, true))
            }

            albumsData.listenLaterAlbums.forEach { album ->
                val existingAlbum = repository.getAlbum(album.id)
                val albumToInsert = album.copy(
                    saveTime = existingAlbum?.saveTime ?: System.currentTimeMillis()
                )
                repository.insertAlbum(albumToInsert)
                repository.insertListenLaterAlbum(IsListenLaterSaved(album.id, true))
            }

            trackRepository.insertTracks(albumsData.trackEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
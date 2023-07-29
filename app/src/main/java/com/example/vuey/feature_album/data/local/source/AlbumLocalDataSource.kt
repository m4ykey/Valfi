package com.example.vuey.feature_album.data.local.source

import com.example.vuey.feature_album.data.local.source.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

interface AlbumLocalDataSource {

    suspend fun insertAlbum(albumEntity: AlbumEntity)
    suspend fun deleteAlbum(albumEntity: AlbumEntity)
    fun getAllAlbums() : Flow<List<AlbumEntity>>
    fun getAlbumById(albumId : String) : Flow<AlbumEntity>
    fun getAlbumCount() : Flow<Int>
    fun getTotalTracks() : Flow<Int>
    fun getTotalLength() : Flow<Int>

}
package com.example.vuey.feature_album.domain.repository

import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail
import com.example.vuey.feature_album.data.remote.model.spotify.album.Album
import com.example.vuey.core.common.network.Resource
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun searchAlbum(albumName : String) : Flow<Resource<List<Album>>>
    suspend fun getAlbum(albumId : String) : Flow<Resource<AlbumDetail>>

    suspend fun insertAlbum(albumEntity: AlbumEntity)
    suspend fun deleteAlbum(albumEntity: AlbumEntity)
    fun getAllAlbums() : Flow<List<AlbumEntity>>
    fun getAlbumById(albumId : String) : Flow<AlbumEntity>
    fun getAlbumCount() : Flow<Int>
    fun getTotalTracks() : Flow<Int>
    fun getTotalLength() : Flow<Int>
    fun searchAlbumInDatabase(searchQuery : String) : Flow<List<AlbumEntity>>

}
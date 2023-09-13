package com.m4ykey.repository.album

import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
import kotlinx.coroutines.flow.Flow

interface AlbumLocalRepository {

    suspend fun insertAlbum(albumEntity: AlbumEntity)
    suspend fun deleteAlbum(albumEntity: AlbumEntity)
    fun getAllAlbums() : Flow<List<AlbumEntity>>
    fun getAlbumById(albumId : String) : Flow<AlbumEntity>
    fun getAlbumCount() : Flow<Int>
    fun getTotalTracks() : Flow<Int>
    fun getTotalLength() : Flow<Int>

    suspend fun insertAlbumToListenLater(listenLaterEntity: ListenLaterEntity)
    suspend fun deleteAlbumToListenLater(listenLaterEntity: ListenLaterEntity)
    fun getAllListenLaterAlbums() : Flow<List<ListenLaterEntity>>
    fun getListenLaterAlbumById(albumId : String) : Flow<ListenLaterEntity>
    fun getListenLaterAlbumCount() : Flow<Int>

}
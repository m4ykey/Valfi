package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.core.network.Resource
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun searchAlbums(query : String) : Flow<PagingData<AlbumItem>>
    suspend fun getAlbumById(id : String) : Flow<Resource<AlbumDetail>>
    fun getAlbumTracks(id : String) : Flow<PagingData<TrackItem>>

    suspend fun insertAlbum(album : AlbumEntity)
    suspend fun deleteAlbum(album: AlbumEntity)
    fun getAlbumSortedAlphabetical() : Flow<PagingData<AlbumEntity>>
    fun getAllAlbumsPaged() : Flow<PagingData<AlbumEntity>>
    suspend fun getLocalAlbumById(albumId : String) : AlbumEntity

}
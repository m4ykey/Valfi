package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.core.network.Resource
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.AlbumWithStates
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun searchAlbums(query : String) : Flow<PagingData<AlbumItem>>
    suspend fun getAlbumById(id : String) : Flow<Resource<AlbumDetail>>
    fun getAlbumTracks(id : String) : Flow<PagingData<TrackItem>>
    fun getNewReleases() : Flow<PagingData<AlbumItem>>

    suspend fun insertAlbum(album : AlbumEntity)
    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved)
    suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved)
    suspend fun getAlbum(albumId : String) : AlbumEntity?
    suspend fun getSavedAlbumState(albumId : String) : IsAlbumSaved?
    suspend fun getListenLaterState(albumId: String) : IsListenLaterSaved?
    suspend fun getAlbumWithStates(albumId : String) : AlbumWithStates?
    suspend fun deleteAlbum(albumId: String)
    suspend fun deleteSavedAlbumState(albumId : String)
    suspend fun deleteListenLaterState(albumId: String)

}
package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.core.network.Resource
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun searchAlbums(query : String) : Flow<PagingData<AlbumItem>>
    suspend fun getAlbumById(id : String) : Flow<Resource<AlbumDetail>>
    fun getAlbumTracks(id : String) : Flow<PagingData<TrackItem>>
    fun getNewReleases() : Flow<PagingData<AlbumItem>>

}
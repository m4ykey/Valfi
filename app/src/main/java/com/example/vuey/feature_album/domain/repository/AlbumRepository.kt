package com.example.vuey.feature_album.domain.repository

import androidx.paging.PagingData
import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumList
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun searchAlbum(albumName : String) : Flow<PagingData<AlbumList>>
    suspend fun getAlbum(albumId : String) : Flow<Resource<AlbumDetail>>

}
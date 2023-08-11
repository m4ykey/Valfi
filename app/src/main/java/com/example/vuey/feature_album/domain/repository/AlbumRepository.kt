package com.example.vuey.feature_album.domain.repository

import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumList
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun searchAlbum(albumName : String) : Flow<Resource<List<AlbumList>>>
    suspend fun getAlbum(albumId : String) : Flow<Resource<AlbumDetail>>

}
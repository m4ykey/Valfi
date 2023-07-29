package com.example.vuey.feature_album.domain.repository

import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_album.data.remote.model.spotify.album.Album
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun searchAlbum(albumName : String) : Flow<Resource<List<Album>>>
    suspend fun getAlbum(albumId : String) : Flow<Resource<AlbumDetail>>

}
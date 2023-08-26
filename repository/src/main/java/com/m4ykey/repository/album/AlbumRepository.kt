package com.m4ykey.repository.album

import com.m4ykey.common.network.Resource
import com.m4ykey.remote.album.model.spotify.album.AlbumDetail
import com.m4ykey.remote.album.model.spotify.album.AlbumList
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun searchAlbum(albumName : String) : Flow<Resource<List<AlbumList>>>
    suspend fun getAlbum(albumId : String) : Flow<Resource<AlbumDetail>>

}
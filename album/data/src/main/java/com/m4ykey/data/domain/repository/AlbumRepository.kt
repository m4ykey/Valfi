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

    fun getAlbumSortedAlphabetical() : Flow<PagingData<AlbumEntity>>
    fun getAllAlbumsPaged() : Flow<PagingData<AlbumEntity>>
    fun getAlbumsOfTypeAlbumPaged() : Flow<PagingData<AlbumEntity>>
    fun getAlbumsOfTypeEPPaged() : Flow<PagingData<AlbumEntity>>
    fun getAlbumsOfTypeSinglePaged() : Flow<PagingData<AlbumEntity>>
    fun getAlbumsOfTypeCompilationPaged() : Flow<PagingData<AlbumEntity>>
    fun searchAlbumsByName(searchQuery : String) : Flow<PagingData<AlbumEntity>>

    fun getListenLaterCount() : Flow<Int>

    suspend fun getLocalAlbumById(albumId : String?) : Flow<AlbumEntity?>

    suspend fun getRandomAlbum() : AlbumEntity?

    suspend fun saveAlbum(album : AlbumEntity)
    suspend fun deleteAlbum(album : AlbumEntity)

    suspend fun updateAlbumSaved(albumId : String, isSaved : Boolean)
    suspend fun updateListenLaterSaved(albumId: String, isListenLater : Boolean)

    fun getListenLaterAlbums() : Flow<PagingData<AlbumEntity>>

    fun getNewReleases() : Flow<PagingData<AlbumItem>>

}
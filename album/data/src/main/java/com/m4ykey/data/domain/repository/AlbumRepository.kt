package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun searchAlbums(query : String, offset : Int, limit : Int) : Flow<List<AlbumItem>>
    suspend fun getAlbumById(id : String) : Flow<AlbumDetail>
    suspend fun getNewReleases(offset : Int, limit : Int) : Flow<List<AlbumItem>>

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

    suspend fun getSavedAlbums() : Flow<PagingData<AlbumEntity>>
    suspend fun getListenLaterAlbums() : Flow<PagingData<AlbumEntity>>
    suspend fun getAlbumType(albumType : String) : Flow<PagingData<AlbumEntity>>
    suspend fun getAlbumSortedByName() : Flow<PagingData<AlbumEntity>>
    suspend fun getSavedAlbumAsc() : Flow<PagingData<AlbumEntity>>

    suspend fun getRandomAlbum() : AlbumEntity?

    fun getListenLaterCount() : Flow<Int>
    fun getAlbumTypeCount(albumType : String) : Flow<Int>
    fun getAlbumCount() : Flow<Int>
    fun getTotalTracksCount() : Flow<Int>

    suspend fun searchAlbumByName(searchQuery : String) : Flow<PagingData<AlbumEntity>>
    suspend fun searchAlbumsListenLater(searchQuery: String) : Flow<PagingData<AlbumEntity>>

}
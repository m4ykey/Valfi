package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.AlbumWithDetails
import com.m4ykey.data.local.model.DecadeResult
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.StarsEntity
import com.m4ykey.data.local.model.relations.AlbumWithStates
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun searchAlbums(query : String, offset : Int, limit : Int) : Flow<PagingData<AlbumItem>>
    suspend fun getAlbumById(id : String) : Flow<AlbumDetail>
    suspend fun getNewReleases(offset : Int, limit : Int) : Flow<PagingData<AlbumItem>>

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

    suspend fun getSavedAlbums() : List<AlbumEntity>
    suspend fun getListenLaterAlbums() : List<AlbumEntity>
    suspend fun getAlbumType(albumType : String) : List<AlbumEntity>
    suspend fun getAlbumSortedByName() : List<AlbumEntity>
    suspend fun getSavedAlbumAsc() : List<AlbumEntity>

    fun getRandomAlbum() : Flow<AlbumEntity?>

    fun getListenLaterCount() : Flow<Int>

    suspend fun searchAlbumByName(searchQuery : String) : List<AlbumEntity>
    suspend fun searchAlbumsListenLater(searchQuery: String) : List<AlbumEntity>

    fun getAlbumCount() : Flow<Int>
    fun getTotalTracksCount() : Flow<Int>
    fun getMostPopularDecade() : Flow<DecadeResult>
    fun getAlbumCountByType(albumType: String) : Flow<Int>
    fun getAlbumWithMostTracks() : Flow<AlbumWithDetails>

    suspend fun insertStars(stars : List<StarsEntity>)
    fun getStarsById(albumId: String) : List<StarsEntity>
    suspend fun deleteStarsById(albumId: String)

}
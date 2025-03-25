package com.m4ykey.data.repository

import com.m4ykey.authentication.interceptor.SpotifyTokenProvider
import com.m4ykey.authentication.interceptor.getToken
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.AlbumWithDetails
import com.m4ykey.data.local.model.DecadeResult
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.StarsEntity
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.data.mapper.toAlbumDetail
import com.m4ykey.data.mapper.toAlbumItem
import com.m4ykey.data.remote.api.AlbumApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi,
    private val tokenProvider: SpotifyTokenProvider,
    private val dao : AlbumDao,
    private val dispatcherIO : CoroutineDispatcher
) : AlbumRepository {

    override suspend fun getNewReleases(offset: Int, limit: Int): Flow<List<AlbumItem>> = flow {
        val result = safeApiCall {
            api.getNewReleases(
                token = getToken(tokenProvider),
                offset = offset,
                limit = limit
            )
        }

        val albums = result.fold(
            onSuccess = { it.albums.items?.map { item -> item.toAlbumItem() } ?: emptyList() },
            onFailure = {
                emptyList()
            }
        )
        emit(albums)
    }.flowOn(dispatcherIO)

    override suspend fun searchAlbums(query: String, offset : Int, limit : Int) : Flow<List<AlbumItem>> = flow {
        val result = safeApiCall {
            api.searchAlbums(
                token = getToken(tokenProvider),
                limit = limit,
                offset = offset,
                query = query
            )
        }

        val albums = result.fold(
            onSuccess = { it.albums.items?.map { item -> item.toAlbumItem() } ?: emptyList() },
            onFailure = {
                emptyList()
            }
        )
        emit(albums)
    }.flowOn(dispatcherIO)

    override suspend fun getAlbumById(id: String): Flow<AlbumDetail> = flow {
        val result = safeApiCall {
            api.getAlbumById(
                token = getToken(tokenProvider),
                id = id
            ).toAlbumDetail()
        }

        result.fold(
            onSuccess = { emit(it) },
            onFailure = { throw it }
        )
    }.flowOn(dispatcherIO)

    override suspend fun insertAlbum(album: AlbumEntity) = withContext(dispatcherIO) {
        dao.insertAlbum(album)
    }

    override suspend fun getAlbum(albumId: String): AlbumEntity? = withContext(dispatcherIO) {
        dao.getAlbum(albumId)
    }

    override suspend fun deleteAlbum(albumId: String) = withContext(dispatcherIO) {
        dao.deleteAlbum(albumId)
    }

    override fun getRandomAlbum(): Flow<AlbumEntity?> = dao.getRandomAlbum()
        .flowOn(dispatcherIO)

    override suspend fun getSavedAlbums(): List<AlbumEntity> = withContext(dispatcherIO) {
        dao.getSavedAlbums()
    }

    override suspend fun getAlbumType(albumType: String): List<AlbumEntity> = withContext(dispatcherIO) {
        dao.getAlbumType(albumType)
    }

    override suspend fun searchAlbumByName(searchQuery: String): List<AlbumEntity> = withContext(dispatcherIO) {
        dao.searchAlbumsByName(searchQuery)
    }

    override suspend fun getAlbumSortedByName(): List<AlbumEntity> = withContext(dispatcherIO) {
        dao.getAlbumSortedByName()
    }

    override suspend fun getSavedAlbumAsc(): List<AlbumEntity> = withContext(dispatcherIO) {
        dao.getSavedAlbumAsc()
    }

    override fun getAlbumCount(): Flow<Int> = dao.getAlbumCount()
        .flowOn(dispatcherIO)

    override fun getTotalTracksCount(): Flow<Int> = dao.getTotalTracksCount()
        .flowOn(dispatcherIO)

    override fun getAlbumCountByType(albumType: String): Flow<Int> = dao.getAlbumCountByType(albumType)
        .flowOn(dispatcherIO)

    override fun getAlbumWithMostTracks(): Flow<AlbumWithDetails> = dao.getAlbumWithMostTracks()
        .flowOn(dispatcherIO)

    override suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved) = withContext(dispatcherIO) {
        dao.insertSavedAlbum(isAlbumSaved)
    }

    override suspend fun getSavedAlbumState(albumId: String): IsAlbumSaved? = withContext(dispatcherIO) {
        dao.getSavedAlbumState(albumId)
    }

    override suspend fun deleteSavedAlbumState(albumId: String) = withContext(dispatcherIO) {
        dao.deleteSavedAlbumState(albumId)
    }

    override suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved) = withContext(dispatcherIO) {
        dao.insertListenLaterAlbum(isListenLaterSaved)
    }

    override suspend fun getListenLaterState(albumId: String): IsListenLaterSaved? = withContext(dispatcherIO) {
        dao.getListenLaterState(albumId)
    }

    override suspend fun deleteListenLaterState(albumId: String) = withContext(dispatcherIO) {
        dao.deleteListenLaterState(albumId)
    }

    override fun getListenLaterCount(): Flow<Int> = dao.getListenLaterCount()
        .flowOn(dispatcherIO)

    override suspend fun getListenLaterAlbums(): List<AlbumEntity> = withContext(dispatcherIO) {
        dao.getListenLaterAlbums()
    }

    override suspend fun searchAlbumsListenLater(searchQuery: String): List<AlbumEntity> = withContext(dispatcherIO) {
        dao.searchAlbumsListenLater(searchQuery)
    }

    override suspend fun getAlbumWithStates(albumId: String): AlbumWithStates? = withContext(dispatcherIO) {
        dao.getAlbumWithStates(albumId)
    }

    override fun getMostPopularDecade(): Flow<DecadeResult> = dao.getMostPopularDecade()
        .flowOn(dispatcherIO)

    override suspend fun insertStars(stars: List<StarsEntity>) = withContext(dispatcherIO) {
        dao.insertStars(stars)
    }

    override fun getStarsById(albumId: String): List<StarsEntity> =  dao.getStarsById(albumId)

    override suspend fun deleteStarsById(albumId: String) = withContext(dispatcherIO) {
        dao.deleteStarsById(albumId)
    }
}
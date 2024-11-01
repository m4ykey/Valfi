package com.m4ykey.data.repository

import com.m4ykey.core.network.safeApiCall
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.data.mapper.toAlbumDetail
import com.m4ykey.data.mapper.toAlbumItem
import com.m4ykey.data.remote.api.AlbumApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi,
    private val tokenProvider: com.m4ykey.authentication.interceptor.SpotifyTokenProvider,
    private val dao : AlbumDao
) : AlbumRepository {

    private val token = runBlocking { "Bearer ${tokenProvider.getAccessToken()}" }

    override suspend fun getNewReleases(offset: Int, limit: Int): Flow<List<AlbumItem>> = flow {
        try {
            val result = api.getNewReleases(token = token, offset = offset, limit = limit)
            val albumResult = result.albums.items?.map { it.toAlbumItem() } ?: emptyList()
            emit(albumResult)
        } catch (e : Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun searchAlbums(query: String, offset : Int, limit : Int) : Flow<List<AlbumItem>> = flow {
        try {
            val result = api.searchAlbums(token = token, query = query, offset = offset, limit = limit)
            val albumResult = result.albums.items?.map { it.toAlbumItem() } ?: emptyList()
            emit(albumResult)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAlbumById(id: String): Flow<AlbumDetail> = flow {
        val result = safeApiCall {
            api.getAlbumById(
                token = token,
                id = id
            ).toAlbumDetail()
        }
        if (result.isSuccess) {
            emit(result.getOrThrow())
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun insertAlbum(album: AlbumEntity) = dao.insertAlbum(album)
    override suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved) = dao.insertSavedAlbum(isAlbumSaved)
    override suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved) = dao.insertListenLaterAlbum(isListenLaterSaved)
    override suspend fun getAlbum(albumId: String): AlbumEntity? = dao.getAlbum(albumId)
    override suspend fun getSavedAlbumState(albumId: String): IsAlbumSaved? = dao.getSavedAlbumState(albumId)
    override suspend fun getListenLaterState(albumId: String): IsListenLaterSaved? = dao.getListenLaterState(albumId)
    override suspend fun getAlbumWithStates(albumId: String): AlbumWithStates? = dao.getAlbumWithStates(albumId)
    override suspend fun deleteAlbum(albumId: String) = dao.deleteAlbum(albumId)
    override suspend fun deleteSavedAlbumState(albumId: String) = dao.deleteSavedAlbumState(albumId)
    override suspend fun deleteListenLaterState(albumId: String) = dao.deleteListenLaterState(albumId)
    override suspend fun getRandomAlbum(): AlbumEntity? = dao.getRandomAlbum()

    override fun getListenLaterCount(): Flow<Int> = dao.getListenLaterCount()

    override suspend fun getSavedAlbums(): List<AlbumEntity> = dao.getSavedAlbums()
    override suspend fun getListenLaterAlbums(): List<AlbumEntity> = dao.getListenLaterAlbums()
    override suspend fun getAlbumType(albumType: String): List<AlbumEntity> = dao.getAlbumType(albumType)
    override suspend fun searchAlbumByName(searchQuery: String): List<AlbumEntity> = dao.searchAlbumsByName(searchQuery)
    override suspend fun searchAlbumsListenLater(searchQuery: String): List<AlbumEntity> = dao.searchAlbumsListenLater(searchQuery)
    override suspend fun getAlbumSortedByName(): List<AlbumEntity> = dao.getAlbumSortedByName()
    override suspend fun getSavedAlbumAsc(): List<AlbumEntity> = dao.getSavedAlbumAsc()
}
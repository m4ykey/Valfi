package com.m4ykey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.Resource
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
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import com.m4ykey.data.remote.paging.NewReleasePagingSource
import com.m4ykey.data.remote.paging.SearchAlbumPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi,
    private val interceptor: SpotifyTokenProvider,
    private val dao : AlbumDao
) : AlbumRepository {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false
    )

    override fun getNewReleases(): Flow<PagingData<AlbumItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                NewReleasePagingSource(
                    api = api,
                    token = interceptor
                )
            }
        ).flow
    }

    override fun searchAlbums(query: String): Flow<PagingData<AlbumItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchAlbumPagingSource(
                    api = api,
                    token = interceptor,
                    query = query
                )
            }
        ).flow
    }

    override suspend fun getAlbumById(id: String): Flow<Resource<AlbumDetail>> = flow {
        emit(Resource.Loading())
        emit(safeApiCall {
            api.getAlbumById(
                token = "Bearer ${interceptor.getAccessToken()}",
                id = id
            ).toAlbumDetail()
        })
    }

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

    override fun getAlbumCount(): Flow<Int> = dao.getAlbumCount()
    override fun getTotalTracksCount(): Flow<Int> = dao.getTotalTracksCount()
    override fun getListenLaterCount(): Flow<Int> = dao.getListenLaterCount()
    override fun getAlbumTypeCount(albumType: String): Flow<Int> = dao.getAlbumTypeCount(albumType)

    override fun getSavedAlbums(): Flow<PagingData<AlbumEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.getSavedAlbums() }
        ).flow
    }

    override fun getListenLaterAlbums(): Flow<PagingData<AlbumEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.getListenLaterAlbums() }
        ).flow
    }

    override fun getAlbumType(albumType: String): Flow<PagingData<AlbumEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.getAlbumType(albumType) }
        ).flow
    }

    override fun searchAlbumByName(searchQuery: String): Flow<PagingData<AlbumEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.searchAlbumsByName(searchQuery) }
        ).flow
    }

    override fun searchAlbumsListenLater(searchQuery: String): Flow<PagingData<AlbumEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.searchAlbumsListenLater(searchQuery) }
        ).flow
    }

    override fun getAlbumSortedByName(): Flow<PagingData<AlbumEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.getAlbumSortedByName() }
        ).flow
    }

    override fun getSavedAlbumDesc(): Flow<PagingData<AlbumEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.getSavedAlbumDesc() }
        ).flow
    }
}
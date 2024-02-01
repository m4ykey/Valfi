package com.m4ykey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.Resource
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.interceptor.SpotifyInterceptor
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.mapper.toAlbumDetail
import com.m4ykey.data.paging.SearchAlbumPagingSource
import com.m4ykey.data.paging.TrackListPagingSource
import com.m4ykey.data.remote.api.AlbumApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi,
    private val interceptor: SpotifyInterceptor,
    private val albumDao: AlbumDao
) : AlbumRepository {
    override fun searchAlbums(query: String): Flow<PagingData<AlbumItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchAlbumPagingSource(
                    api = api,
                    interceptor = interceptor,
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

    override fun getAlbumTracks(id: String): Flow<PagingData<TrackItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TrackListPagingSource(
                    id = id,
                    interceptor = interceptor,
                    api = api
                )
            }
        ).flow
    }

    override suspend fun insertAlbum(album: AlbumEntity) = albumDao.insertAlbum(album)
    override suspend fun deleteAlbum(album: AlbumEntity) = albumDao.deleteAlbum(album)
    override fun getAlbumSortedAlphabetical(): Flow<List<AlbumEntity>> = albumDao.getAlbumSortedAlphabetical()

    override fun getAllAlbumsPaged(): Flow<PagingData<AlbumEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { albumDao.getAllAlbumsPaged() }
        ).flow
    }
}
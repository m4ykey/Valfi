package com.m4ykey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.Resource
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.data.domain.model.AlbumDetail
import com.m4ykey.data.domain.model.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.interceptor.SpotifyInterceptor
import com.m4ykey.data.mapper.toAlbumDetail
import com.m4ykey.data.paging.SearchAlbumPagingSource
import com.m4ykey.data.remote.api.AlbumApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val api : AlbumApi,
    private val interceptor: SpotifyInterceptor
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
}
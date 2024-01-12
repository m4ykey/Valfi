package com.m4ykey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.data.domain.model.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.interceptor.SpotifyInterceptor
import com.m4ykey.data.paging.SearchAlbumPagingSource
import com.m4ykey.data.remote.api.AlbumApi
import kotlinx.coroutines.flow.Flow
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
}
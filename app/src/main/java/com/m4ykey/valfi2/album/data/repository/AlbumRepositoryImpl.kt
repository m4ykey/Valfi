package com.m4ykey.valfi2.album.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.m4ykey.valfi2.album.data.domain.model.Item
import com.m4ykey.valfi2.album.data.domain.repository.AlbumRepository
import com.m4ykey.valfi2.album.data.remote.api.AlbumApi
import com.m4ykey.valfi2.album.data.remote.interceptor.SpotifyInterceptor
import com.m4ykey.valfi2.album.data.remote.paging.SearchAlbumPagingSource
import kotlinx.coroutines.flow.Flow

class AlbumRepositoryImpl(
    private val interceptor: SpotifyInterceptor,
    private val api: AlbumApi
) : AlbumRepository {

    override fun searchAlbums(): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 20 + (20 * 2),
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchAlbumPagingSource(
                    api = api,
                    interceptor = interceptor
                )
            }
        ).flow
    }
}
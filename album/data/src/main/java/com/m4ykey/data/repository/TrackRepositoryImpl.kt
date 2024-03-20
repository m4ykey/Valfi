package com.m4ykey.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import com.m4ykey.core.Constants
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.dao.TrackDao
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import com.m4ykey.data.remote.paging.TrackListRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val api : AlbumApi,
    private val db : AlbumDatabase,
    private val interceptor : SpotifyTokenProvider,
    private val dao : TrackDao
) : TrackRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getAlbumTracks(id: String): Flow<PagingData<TrackEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = TrackListRemoteMediator(
                id = id,
                api = api,
                interceptor = interceptor,
                database = db
            ),
            pagingSourceFactory = { dao.getTrackPagingSource(id) }
        ).flow.map { pagingData ->
            pagingData.filter { trackEntity ->
                trackEntity.albumId == id
            }
        }
    }

}
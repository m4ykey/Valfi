package com.m4ykey.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.data.mapper.toTrackEntity
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TrackListRemoteMediator @Inject constructor(
    private val id: String,
    private val api : AlbumApi,
    private val interceptor : SpotifyTokenProvider,
    private val database : AlbumDatabase
) : RemoteMediator<Int, TrackEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TrackEntity>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.id
                }
            }

            val page = loadKey?.toInt() ?: 0
            val limit = state.config.pageSize

            val response = api.getAlbumTracks(
                id = id,
                limit = limit,
                offset = page * limit,
                token = "Bearer ${interceptor.getAccessToken()}"
            )

            val items = response.items.map { it.toTrackEntity(id) }
            val albumWithStates = database.albumDao().getAlbumWithStates(id)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.trackDao().deleteTracks(id)
                }
                database.trackDao().insertTracks(items)
            }

            return MediatorResult.Success(endOfPaginationReached = response.next.isNullOrEmpty())
        } catch (e : Exception) {
            return MediatorResult.Error(e)
        }
    }
}
package com.example.vuey.feature_album.data.repository

import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_album.data.remote.api.AlbumApi
import com.example.vuey.feature_album.data.remote.model.spotify.album.Album
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail
import com.example.vuey.feature_album.data.remote.token.SpotifyInterceptor
import com.example.vuey.feature_album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val albumApi: AlbumApi,
    private val spotifyInterceptor: SpotifyInterceptor
) : AlbumRepository {

    override suspend fun searchAlbum(albumName: String): Flow<Resource<List<Album>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val albumResponse = albumApi.searchAlbum(
                    query = albumName,
                    token = "Bearer ${spotifyInterceptor.getAccessToken()}"
                ).albums.items
                emit(Resource.Success(albumResponse))
            } catch (e : HttpException) {
                throw IOException(e.localizedMessage ?: "An unexpected error occurred")
            }
        }.catch { e ->
            emit(Resource.Failure(message = e.localizedMessage ?: "No internet connection"))
        }
    }

    override suspend fun getAlbum(albumId: String): Flow<Resource<AlbumDetail>> {
        return flow {
            emit(Resource.Loading())

            try {
                val albumResponse = albumApi.getAlbum(
                    token = "Bearer ${spotifyInterceptor.getAccessToken()}",
                    albumId = albumId
                )
                emit(Resource.Success(albumResponse))
            } catch (e: HttpException) {
                throw IOException(e.localizedMessage ?: "An unexpected error occurred")
            }
        }.catch { e ->
            emit(Resource.Failure(message = e.localizedMessage ?: "No internet connection"))
        }
    }
}
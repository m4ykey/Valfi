package com.m4ykey.repository.album

import com.m4ykey.common.network.Resource
import com.m4ykey.remote.album.api.AlbumApi
import com.m4ykey.remote.album.model.spotify.album.AlbumDetail
import com.m4ykey.remote.album.model.spotify.album.AlbumList
import com.m4ykey.remote.album.token.SpotifyInterceptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumApi: AlbumApi,
    private val spotifyInterceptor: SpotifyInterceptor
) : AlbumRepository {

    override suspend fun searchAlbum(albumName: String): Flow<Resource<List<AlbumList>>> {
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
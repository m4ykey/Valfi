package com.example.vuey.feature_artist.data.repository

import com.example.vuey.feature_album.data.remote.token.SpotifyInterceptor
import com.example.vuey.feature_artist.data.remote.api.ArtistSpotifyApi
import com.example.vuey.feature_artist.data.remote.api.ArtistLastFmApi
import com.example.vuey.feature_artist.data.remote.model.last_fm.ArtistBio
import com.example.vuey.feature_artist.data.remote.model.spotify.artist.ArtistInfo
import com.example.vuey.feature_artist.data.remote.model.spotify.top_tracks.Track
import com.example.vuey.feature_artist.domain.repository.ArtistRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val artistApi: ArtistSpotifyApi,
    private val artistBioApi: ArtistLastFmApi,
    private val spotifyInterceptor: SpotifyInterceptor
) : ArtistRepository {
    override suspend fun getArtistBio(artistName: String): Flow<Resource<ArtistBio>> {
        return flow {
            emit(Resource.Loading())

            try {
                val artistResponse = artistBioApi.getArtistBio(artistName = artistName).artist
                emit(Resource.Success(artistResponse))
            } catch (e : HttpException) {
                throw IOException(e.localizedMessage ?: "An unexpected error occurred")
            }
        }.catch { e ->
            emit(Resource.Failure(message = e.localizedMessage ?: "No internet connection"))
        }
    }

    override suspend fun getArtistTopTracks(artistId: String): Flow<Resource<List<Track>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val topTracksResponse = artistApi.getArtistTopTracks(
                    artistId = artistId,
                    token = "Bearer ${spotifyInterceptor.getAccessToken()}"
                ).tracks
                emit(Resource.Success(topTracksResponse))
            } catch (e : HttpException) {
                throw IOException(e.localizedMessage ?: "An unexpected error occurred")
            }
        }.catch { e ->
            emit(Resource.Failure(message = e.localizedMessage ?: "No internet connection"))
        }
    }

    override suspend fun getArtistInfo(artistId: String): Flow<Resource<ArtistInfo>> {
        return flow {
            emit(Resource.Loading())

            try {
                val artistResponse = artistApi.getArtistInfo(
                    artistId = artistId,
                    token = "Bearer ${spotifyInterceptor.getAccessToken()}",
                )
                emit(Resource.Success(artistResponse))
            } catch (e : HttpException) {
                throw IOException(e.localizedMessage ?: "An unexpected error occurred")
            }
        }.catch { e ->
            emit(Resource.Failure(message = e.localizedMessage ?: "No internet connection"))
        }
    }
}
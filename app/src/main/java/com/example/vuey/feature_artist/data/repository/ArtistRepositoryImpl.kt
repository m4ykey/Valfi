package com.example.vuey.feature_artist.data.repository

import com.example.vuey.feature_artist.data.remote.api.ArtistApi
import com.example.vuey.feature_artist.data.remote.model.last_fm.ArtistBio
import com.example.vuey.feature_artist.domain.repository.ArtistRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val artistApi: ArtistApi
) : ArtistRepository {
    override suspend fun getArtistBio(artistName: String): Flow<Resource<ArtistBio>> {
        return flow {
            emit(Resource.Loading())

            try {
                val artistResponse = artistApi.getArtistBio(artistName = artistName).artist
                emit(Resource.Success(artistResponse))
            } catch (e : IOException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "No internet connection",
                        data = null
                    )
                )
            } catch (e : HttpException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "An unexpected error occurred",
                        data = null
                    )
                )
            }
        }
    }
}
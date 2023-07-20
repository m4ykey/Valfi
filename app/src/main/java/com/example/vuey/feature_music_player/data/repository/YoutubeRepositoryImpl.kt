package com.example.vuey.feature_music_player.data.repository

import com.example.vuey.feature_music_player.data.remote.api.YoutubeApi
import com.example.vuey.feature_music_player.data.remote.model.name.YoutubeSearchItems
import com.example.vuey.feature_music_player.domain.repository.YoutubeRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class YoutubeRepositoryImpl @Inject constructor(
    private val youtubeApi: YoutubeApi
) : YoutubeRepository {
    override suspend fun getYoutubeVideoByName(videoName: String): Flow<Resource<List<YoutubeSearchItems>>> {

        return flow {
            emit(Resource.Loading())
            try {
                val youtubeVideoResponse = youtubeApi.getYoutubeVideoByName(
                    query = videoName
                ).items
                emit(Resource.Success(youtubeVideoResponse))
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
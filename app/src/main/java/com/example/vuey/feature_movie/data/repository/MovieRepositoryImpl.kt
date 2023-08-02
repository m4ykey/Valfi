package com.example.vuey.feature_movie.data.repository

import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_movie.data.remote.api.MovieApi
import com.example.vuey.feature_movie.data.remote.model.MovieCast
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.feature_movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : MovieRepository {

    override suspend fun searchMovie(query: String): Flow<Resource<List<MovieList>>> {
        return flow {

            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.searchMovie(query).results
                emit(Resource.Success(movieResponse))
            } catch (e : HttpException) {
                throw IOException(e.localizedMessage ?: "An unexpected error occurred")
            }
        }.catch { e ->
            emit(Resource.Failure(message = e.localizedMessage ?: "No internet connection"))
        }
    }

    override suspend fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>> {
        return flow {
            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.getMovieDetail(movieId)
                emit(Resource.Success(movieResponse))
            } catch (e : HttpException) {
                throw IOException(e.localizedMessage ?: "An unexpected error occurred")
            }
        }.catch { e ->
            emit(Resource.Failure(message = e.localizedMessage ?: "No internet connection"))
        }
    }

    override suspend fun getMovieCast(movieId: Int): Flow<Resource<List<MovieCast.CastDetail>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.getMovieCast(movieId).cast
                emit(Resource.Success(movieResponse))
            } catch (e : HttpException) {
                throw IOException(e.localizedMessage ?: "An unexpected error occurred")
            }
        }.catch { e ->
            emit(Resource.Failure(message = e.localizedMessage ?: "No internet connection"))
        }
    }
}
package com.m4ykey.repository.movie

import com.m4ykey.common.network.Resource
import com.m4ykey.remote.movie.api.MovieApi
import com.m4ykey.remote.movie.model.MovieCast
import com.m4ykey.remote.movie.model.MovieDetail
import com.m4ykey.remote.movie.model.MovieList
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
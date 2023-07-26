package com.example.vuey.feature_movie.data.repository

import com.example.vuey.feature_movie.data.local.dao.MovieDao
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.remote.api.MovieApi
import com.example.vuey.feature_movie.data.remote.model.MovieCast
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun insertMovie(movieEntity: MovieEntity) {
        return movieDao.insertMovie(movieEntity)
    }

    override suspend fun deleteMovie(movieEntity: MovieEntity) {
        return movieDao.deleteMovie(movieEntity)
    }

    override fun getAllMovies(): Flow<List<MovieEntity>> {
        return movieDao.getAllMovies()
    }

    override fun getMovieById(movieId: Int): Flow<MovieEntity> {
        return movieDao.getMovieById(movieId)
    }

    override fun searchMovieInDatabase(movieTitle: String): Flow<List<MovieEntity>> {
        return movieDao.searchMovieInDatabase(movieTitle)
    }

    override suspend fun searchMovie(query: String): Flow<Resource<List<MovieList>>> {
        return flow {

            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.searchMovie(query).results
                emit(Resource.Success(movieResponse))
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
                ))
            }
        }
    }

    override suspend fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>> {
        return flow {
            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.getMovieDetail(movieId)
                emit(Resource.Success(movieResponse))
            } catch (e : IOException) {
                emit(
                    Resource.Failure(
                    message = e.localizedMessage ?: "No internet connection",
                    data = null
                ))
            } catch (e : HttpException) {
                emit(
                    Resource.Failure(
                    data = null,
                    message = e.localizedMessage ?: "An unexpected error occurred"
                ))
            }
        }
    }

    override suspend fun getMovieCast(movieId: Int): Flow<Resource<List<MovieCast.CastDetail>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.getMovieCast(movieId).cast
                emit(Resource.Success(movieResponse))
            } catch (e : IOException) {
                emit(
                    Resource.Failure(
                    message = e.localizedMessage ?: "No internet connection",
                    data = null
                ))
            } catch (e : HttpException) {
                emit(
                    Resource.Failure(
                    data = null,
                    message = e.localizedMessage ?: "An unexpected error occurred"
                ))
            }
        }
    }

    override fun getMovieCount(): Flow<Int> {
        return movieDao.getMovieCount()
    }

    override fun getTotalLength(): Flow<Int> {
        return movieDao.getTotalLength()
    }
}
package com.example.vuey.feature_movie.data.local.source

import com.example.vuey.feature_movie.data.local.source.dao.MovieDao
import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieLocalDataSource {

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

    override fun getMovieCount(): Flow<Int> {
        return movieDao.getMovieCount()
    }

    override fun getTotalLength(): Flow<Int> {
        return movieDao.getTotalLength()
    }
}
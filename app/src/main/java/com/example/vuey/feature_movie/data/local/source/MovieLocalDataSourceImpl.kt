package com.example.vuey.feature_movie.data.local.source

import com.example.vuey.feature_movie.data.local.source.dao.MovieDao
import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import com.example.vuey.feature_movie.data.local.source.entity.WatchLaterEntity
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

    override fun getMovieCount(): Flow<Int> {
        return movieDao.getMovieCount()
    }

    override fun getTotalLength(): Flow<Int> {
        return movieDao.getTotalLength()
    }

    override suspend fun insertWatchLaterMovie(watchLaterEntity: WatchLaterEntity) {
        return movieDao.insertWatchLaterMovie(watchLaterEntity)
    }

    override suspend fun deleteWatchLaterMovie(watchLaterEntity: WatchLaterEntity) {
        return movieDao.deleteWatchLaterMovie(watchLaterEntity)
    }

    override fun getAllWatchLaterMovies(): Flow<List<WatchLaterEntity>> {
        return movieDao.getAllWatchLaterMovies()
    }

    override fun getWatchLaterMovieById(movieId: Int): Flow<WatchLaterEntity> {
        return movieDao.getWatchLaterMovieById(movieId)
    }
}
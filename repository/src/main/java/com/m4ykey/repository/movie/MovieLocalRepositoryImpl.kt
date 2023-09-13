package com.m4ykey.repository.movie

import com.m4ykey.local.movie.dao.MovieDao
import com.m4ykey.local.movie.entity.MovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieLocalRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieLocalRepository {

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

    override fun getWatchLaterMovieCount(): Flow<Int> {
        return movieDao.getWatchLaterMovieCount()
    }
}
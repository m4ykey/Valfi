package com.m4ykey.repository.movie

import com.m4ykey.local.movie.entity.MovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity
import kotlinx.coroutines.flow.Flow

interface MovieLocalRepository {

    suspend fun insertMovie(movieEntity: MovieEntity)
    suspend fun deleteMovie(movieEntity: MovieEntity)
    fun getAllMovies() : Flow<List<MovieEntity>>
    fun getMovieById(movieId : Int) : Flow<MovieEntity>
    fun getMovieCount() : Flow<Int>
    fun getTotalLength() : Flow<Int>

    suspend fun insertWatchLaterMovie(watchLaterEntity: WatchLaterEntity)
    suspend fun deleteWatchLaterMovie(watchLaterEntity: WatchLaterEntity)
    fun getAllWatchLaterMovies() : Flow<List<WatchLaterEntity>>
    fun getWatchLaterMovieById(movieId: Int) : Flow<WatchLaterEntity>
    fun getWatchLaterMovieCount() : Flow<Int>

}
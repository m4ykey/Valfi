package com.example.vuey.feature_movie.data.local.source

import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import com.example.vuey.feature_movie.data.local.source.entity.WatchLaterEntity
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {

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

}
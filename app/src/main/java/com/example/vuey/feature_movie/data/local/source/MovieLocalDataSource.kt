package com.example.vuey.feature_movie.data.local.source

import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {

    suspend fun insertMovie(movieEntity: MovieEntity)
    suspend fun deleteMovie(movieEntity: MovieEntity)
    fun getAllMovies() : Flow<List<MovieEntity>>
    fun getMovieById(movieId : Int) : Flow<MovieEntity>
    fun searchMovieInDatabase(movieTitle : String) : Flow<List<MovieEntity>>
    fun getMovieCount() : Flow<Int>
    fun getTotalLength() : Flow<Int>

}
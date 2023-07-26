package com.example.vuey.feature_movie.domain.repository

import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.remote.model.MovieCast
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.core.common.network.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun searchMovie(query : String) : Flow<Resource<List<MovieList>>>
    suspend fun getMovieDetail(movieId : Int) : Flow<Resource<MovieDetail>>
    suspend fun getMovieCast(movieId: Int) : Flow<Resource<List<MovieCast.CastDetail>>>

    suspend fun insertMovie(movieEntity: MovieEntity)
    suspend fun deleteMovie(movieEntity: MovieEntity)
    fun getAllMovies() : Flow<List<MovieEntity>>
    fun getMovieById(movieId : Int) : Flow<MovieEntity>
    fun searchMovieInDatabase(movieTitle : String) : Flow<List<MovieEntity>>

    fun getMovieCount() : Flow<Int>
    fun getTotalLength() : Flow<Int>
}
package com.m4ykey.repository.movie

import com.m4ykey.common.network.Resource
import com.m4ykey.remote.movie.model.MovieCast
import com.m4ykey.remote.movie.model.MovieDetail
import com.m4ykey.remote.movie.model.MovieList
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun searchMovie(query : String) : Flow<Resource<List<MovieList>>>
    suspend fun getMovieDetail(movieId : Int) : Flow<Resource<MovieDetail>>
    suspend fun getMovieCast(movieId: Int) : Flow<Resource<List<MovieCast.CastDetail>>>

}
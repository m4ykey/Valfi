package com.example.vuey.feature_movie.domain.repository

import androidx.paging.PagingData
import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_movie.data.remote.model.MovieCast
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.MovieList
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun searchMovie(query : String) : Flow<PagingData<MovieList>>
    suspend fun getMovieDetail(movieId : Int) : Flow<Resource<MovieDetail>>
    suspend fun getMovieCast(movieId: Int) : Flow<Resource<List<MovieCast.CastDetail>>>

}
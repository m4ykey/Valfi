package com.m4ykey.remote.movie.api

import com.m4ykey.remote.movie.model.MovieCast
import com.m4ykey.remote.movie.model.MovieDetail
import com.m4ykey.remote.movie.model.SearchMovie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface MovieApi {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query : String,
        @Query("language") language : String = Locale.getDefault().language,
    ) : SearchMovie

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId : Int,
        @Query("language") language : String = Locale.getDefault().language
    ) : MovieDetail

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movieId: Int
    ) : MovieCast

}
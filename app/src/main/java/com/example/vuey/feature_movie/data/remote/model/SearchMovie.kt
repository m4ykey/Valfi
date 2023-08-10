package com.example.vuey.feature_movie.data.remote.model

data class SearchMovie(
    val page : Int,
    val results: List<MovieList>
)
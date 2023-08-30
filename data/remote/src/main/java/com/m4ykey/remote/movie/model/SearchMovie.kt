package com.m4ykey.remote.movie.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchMovie(
    val results: List<MovieList>
)
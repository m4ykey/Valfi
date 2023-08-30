package com.m4ykey.remote.movie.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class MovieList(
    val id: Int,
    val overview: String,
    @field:Json(name = "poster_path")
    val posterPath: String?,
    @field:Json(name = "release_date")
    val releaseDate: String,
    val title: String,
    @field:Json(name = "vote_average")
    val voteAverage: Double,
) : Parcelable
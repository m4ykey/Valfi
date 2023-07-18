package com.example.vuey.feature_movie.data.remote.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genres")
    val genreList: List<Genre>,
    val id: Int,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    val runtime: Int,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
)
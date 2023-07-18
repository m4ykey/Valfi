package com.example.vuey.feature_movie.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieList(
    val id: Int,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val vote_average: Double,
) : Parcelable
package com.m4ykey.remote.movie.model

import com.google.gson.annotations.SerializedName

data class MovieCast(
    val cast: List<CastDetail>,
    val id: Int
) {
    data class CastDetail(
        @SerializedName("cast_id")
        val castId: Int,
        val name: String,
        @SerializedName("profile_path")
        val profilePath: String?
    )
}
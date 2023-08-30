package com.m4ykey.remote.movie.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieCast(
    val cast: List<CastDetail>,
    val id: Int
) {
    @JsonClass(generateAdapter = true)
    data class CastDetail(
        @field:Json(name = "cast_id")
        val castId: Int,
        val name: String,
        @field:Json(name = "profile_path")
        val profilePath: String?
    )
}
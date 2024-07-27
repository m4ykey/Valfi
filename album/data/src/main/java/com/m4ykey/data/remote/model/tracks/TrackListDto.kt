package com.m4ykey.data.remote.model.tracks

import androidx.annotation.Keep

@Keep
data class TrackListDto(
    val items: List<TrackItemDto>,
    val next: String?,
    val previous: String?
)
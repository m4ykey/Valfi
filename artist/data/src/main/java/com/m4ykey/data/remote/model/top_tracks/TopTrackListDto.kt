package com.m4ykey.data.remote.model.top_tracks

import androidx.annotation.Keep

@Keep
data class TopTrackListDto(
    val tracks: List<TrackDto>?
)
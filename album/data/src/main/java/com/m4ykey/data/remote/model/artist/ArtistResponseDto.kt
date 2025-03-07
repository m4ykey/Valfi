package com.m4ykey.data.remote.model.artist

import androidx.annotation.Keep

@Keep
data class ArtistResponseDto(
    val artists : List<ArtistListDto>
)

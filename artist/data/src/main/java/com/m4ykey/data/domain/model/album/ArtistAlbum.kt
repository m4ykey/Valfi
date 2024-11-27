package com.m4ykey.data.domain.model.album

import com.m4ykey.data.domain.model.Image

data class ArtistAlbum(
    val albumGroup: String,
    val id: String,
    val images: List<Image>,
    val name: String
)

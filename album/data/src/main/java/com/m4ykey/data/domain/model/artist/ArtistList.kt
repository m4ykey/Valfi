package com.m4ykey.data.domain.model.artist

import com.m4ykey.data.domain.model.album.ExternalUrls
import com.m4ykey.data.domain.model.album.Image

data class ArtistList(
    val name : String,
    val externalUrls: ExternalUrls,
    val id : String,
    val images : List<Image>
)

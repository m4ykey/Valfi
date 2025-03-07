package com.m4ykey.data.domain.model.album

import com.m4ykey.data.domain.model.artist.Artist

data class AlbumDetail(
    val albumType : String,
    val artists : List<Artist>,
    val externalUrls: ExternalUrls,
    val id : String,
    val images : List<Image>,
    val name : String,
    val releaseDate : String,
    val totalTracks : Int,
    val copyrights : List<Copyrights>
)

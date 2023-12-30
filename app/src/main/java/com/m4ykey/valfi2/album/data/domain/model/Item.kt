package com.m4ykey.valfi2.album.data.domain.model

data class Item(
    val albumType : String,
    val artists : List<Artist>,
    val externalUrls : ExternalUrls,
    val id : String,
    val images : List<Image>,
    val name : String,
    val releaseDate : String,
    val totalTracks : Int
)

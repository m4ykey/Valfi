package com.m4ykey.data.domain.model

data class Artist(
    val externalUrls: ExternalUrls,
    val followers: Followers,
    val genres: List<String?>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)

package com.m4ykey.data.remote.model

data class NewsDto(
    val articles: List<ArticleDto>?,
    val status: String?,
    val totalResults: Int?
)
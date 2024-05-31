package com.m4ykey.data.domain.model

import com.m4ykey.data.remote.model.SourceDto

data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)

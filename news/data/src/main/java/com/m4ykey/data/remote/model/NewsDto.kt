package com.m4ykey.data.remote.model

import androidx.annotation.Keep

@Keep
data class NewsDto(
    val articles: List<ArticleDto>?
)
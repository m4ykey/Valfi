package com.m4ykey.data.mapper

import com.m4ykey.data.domain.model.Article
import com.m4ykey.data.domain.model.Source
import com.m4ykey.data.remote.model.ArticleDto
import com.m4ykey.data.remote.model.SourceDto

fun ArticleDto.toArticle() : Article =
    Article(
        author = author ?: "",
        content = content ?: "",
        description = description ?: "",
        publishedAt = publishedAt ?: "",
        url = url ?: "",
        title = title ?: "",
        source = source?.toSource() ?: Source(),
        urlToImage = urlToImage ?: ""
    )

fun SourceDto.toSource() : Source =
    Source(name = name ?: "")
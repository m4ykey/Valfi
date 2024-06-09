package com.m4ykey.data.domain.repository

import com.m4ykey.data.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getMusicNews(page: Int, pageSize : Int) : Flow<List<Article>>

}
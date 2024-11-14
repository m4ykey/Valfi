package com.m4ykey.data.repository

import com.m4ykey.data.domain.model.Article
import com.m4ykey.data.domain.repository.NewsRepository
import com.m4ykey.data.mapper.toArticle
import com.m4ykey.data.remote.api.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val api : NewsApi) : NewsRepository {

    override fun getMusicNews(page: Int, pageSize: Int, sortBy : String): Flow<List<Article>> = flow {
        emit(emptyList())
        try {
            val result = api.getMusicNews(page = page, pageSize = pageSize, sortBy = sortBy)
            val newsResult = result.articles?.map { it.toArticle() } ?: emptyList()
            emit(newsResult)
        } catch (e : Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
}
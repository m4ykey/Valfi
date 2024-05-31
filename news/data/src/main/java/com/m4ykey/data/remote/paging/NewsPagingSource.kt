package com.m4ykey.data.remote.paging

import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.data.domain.model.Article
import com.m4ykey.data.mapper.toArticle
import com.m4ykey.data.remote.api.NewsApi
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    override val api : NewsApi
) : BasePagingSource<Article>(api) {

    override suspend fun loadPage(params: LoadParams<Int>, page: Int, limit: Int?): List<Article> {
        val response = api.getMusicNews(
            page = page,
            pageSize = params.loadSize
        ).articles

        return response?.map { it!!.toArticle() }.orEmpty()
    }
}
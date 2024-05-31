package com.m4ykey.data.remote.paging

import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.data.domain.model.Article
import com.m4ykey.data.mapper.toArticle
import com.m4ykey.data.remote.api.NewsApi
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    override val api : NewsApi
) : BasePagingSource<Article>(api) {

    override suspend fun loadPage(params: LoadParams<Int>, page: Int): List<Article> {
        return api.getMusicNews(
            page = page + 1,
            pageSize = params.loadSize
        ).articles?.map { it.toArticle() }.orEmpty()
    }
}
package com.m4ykey.data.repository

import androidx.paging.PagingData
import com.m4ykey.core.paging.createPager
import com.m4ykey.data.domain.model.Article
import com.m4ykey.data.domain.repository.NewsRepository
import com.m4ykey.data.remote.api.NewsApi
import com.m4ykey.data.remote.paging.NewsPagingSource
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(private val api : NewsApi) : NewsRepository {

    override fun getMusicNews(): Flow<PagingData<Article>> = createPager {
        NewsPagingSource(api = api)
    }
}
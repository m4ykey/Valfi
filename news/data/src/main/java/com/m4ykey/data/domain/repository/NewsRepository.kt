package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getMusicNews() : Flow<PagingData<Article>>

}
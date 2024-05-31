package com.m4ykey.ui

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.Article

data class NewsUiState(
    val isLoading : Boolean = false,
    val error : String? = null,
    val newsList : PagingData<Article>? = null
)

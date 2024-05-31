package com.m4ykey.core.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.m4ykey.core.Constants
import kotlinx.coroutines.flow.Flow

private val pagingConfig = PagingConfig(
    pageSize = Constants.PAGE_SIZE,
    enablePlaceholders = false
)

fun <T: Any> createPager(pagingSourceFactory: () -> PagingSource<Int, T>) : Flow<PagingData<T>> {
    return Pager(
        config = pagingConfig,
        pagingSourceFactory = pagingSourceFactory
    ).flow
}
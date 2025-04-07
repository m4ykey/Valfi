package com.m4ykey.core.paging

import androidx.paging.PagingConfig
import com.m4ykey.core.Constants.PAGE_SIZE

val pagingConfig = PagingConfig(
    pageSize = PAGE_SIZE,
    enablePlaceholders = false
)
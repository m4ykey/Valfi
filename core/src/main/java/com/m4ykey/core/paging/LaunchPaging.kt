package com.m4ykey.core.paging

import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T: Any> launchPaging(
    scope : CoroutineScope,
    source : suspend () -> Flow<PagingData<T>>,
    onDataCollected : (PagingData<T>) -> Unit
) {
    scope.launch {
        source()
            .cachedIn(this)
            .collect { pagingData ->
                onDataCollected(pagingData)
            }
    }
}
package com.m4ykey.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<Value : Any> : PagingSource<Int, Value>() {

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey
                ?: state.closestPageToPosition(position)?.nextKey
        }
    }

    override suspend fun load(params : LoadParams<Int>) : LoadResult<Int, Value> {
        val position = params.key ?: 0
        val limit = params.loadSize.coerceIn(1, 20)

        return try {
            val result = loadData(position, limit)
            val data = result.getOrNull() ?: emptyList()

            val nextKey = if (data.isEmpty() || data.size < limit) null else position + limit

            LoadResult.Page(
                data = data,
                nextKey = nextKey,
                prevKey = if (position == 0) null else position - limit
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }

    protected abstract suspend fun loadData(offset : Int, limit : Int) : Result<List<Value>>

}
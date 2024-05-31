package com.m4ykey.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.m4ykey.core.network.NetworkError
import retrofit2.HttpException
import java.io.IOException

abstract class BasePagingSource<T: Any>(open val api : Any) : PagingSource<Int, T>() {

    abstract suspend fun loadPage(params : LoadParams<Int>, page : Int) : List<T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { closestPage ->
                closestPage.nextKey ?: closestPage.prevKey
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: 0
            val response = loadPage(params, page)

            LoadResult.Page(
                data = response,
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (response.isNotEmpty()) page + 1 else null
            )
        } catch (e : IOException) {
            LoadResult.Error(NetworkError.HttpError(e.message, e))
        } catch (e : HttpException) {
            LoadResult.Error(NetworkError.NoInternetConnection(e.message(), e))
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}
package com.example.vuey.feature_movie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vuey.core.common.Constants.STARTING_PAGE_INDEX
import com.example.vuey.feature_movie.data.remote.api.MovieApi
import com.example.vuey.feature_movie.data.remote.model.MovieList
import retrofit2.HttpException

class MoviePagingSource(
    private val movieApi: MovieApi,
    private val query : String
) : PagingSource<Int, MovieList>() {

    override fun getRefreshKey(state: PagingState<Int, MovieList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieList> {

        return try {
            val currentPage = params.key ?: STARTING_PAGE_INDEX
            val response = movieApi.searchMovie(
                query = query,
                page = currentPage
            ).results
            val responseData = mutableListOf<MovieList>()
            responseData.addAll(response)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == STARTING_PAGE_INDEX) null else - 1,
                nextKey = currentPage.plus(1)
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        } catch (e : HttpException) {
            LoadResult.Error(e)
        }
    }

}
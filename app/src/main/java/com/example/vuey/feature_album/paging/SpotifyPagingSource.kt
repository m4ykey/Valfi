package com.example.vuey.feature_album.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vuey.feature_album.data.remote.api.AlbumApi
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumList
import com.example.vuey.feature_album.data.remote.token.SpotifyInterceptor
import retrofit2.HttpException

class SpotifyPagingSource(
    private val albumApi: AlbumApi,
    private val query : String,
    private val spotifyInterceptor: SpotifyInterceptor
) : PagingSource<Int, AlbumList>() {

    override fun getRefreshKey(state: PagingState<Int, AlbumList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            val prevKey = anchorPage?.prevKey
            val nextKey = anchorPage?.nextKey

            if (prevKey != null && prevKey in 0..50) {
                return prevKey
            } else if (nextKey != null && prevKey in 0..50) {
                return nextKey
            }
            return null
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumList> {
        return try {
            val currentPage = params.key ?: 0 // Set default to 0 if the key is null
            val limit = params.loadSize.coerceIn(1, 50) // Limit to the range between 1 and 50

            val response = albumApi.searchAlbum(
                query = query,
                limit = limit,
                offset = currentPage * limit,
                token = "Bearer ${spotifyInterceptor.getAccessToken()}"
            ).albums.items
            val responseData = mutableListOf<AlbumList>()
            responseData.addAll(response)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = currentPage + 1
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        } catch (e : HttpException) {
            LoadResult.Error(e)
        }
    }
}
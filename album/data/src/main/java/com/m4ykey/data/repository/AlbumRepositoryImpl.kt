package com.m4ykey.data.repository

import androidx.paging.PagingData
import com.m4ykey.core.network.Resource
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.core.paging.createPager
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.data.mapper.toAlbumDetail
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import com.m4ykey.data.remote.paging.NewReleasePagingSource
import com.m4ykey.data.remote.paging.SearchAlbumPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi,
    private val token: SpotifyTokenProvider,
    private val dao : AlbumDao
) : AlbumRepository {

    override fun getNewReleases(): Flow<PagingData<AlbumItem>> = createPager {
        NewReleasePagingSource(api, token)
    }

    override fun searchAlbums(query: String): Flow<PagingData<AlbumItem>> = createPager {
        SearchAlbumPagingSource(api = api, query = query, token = token)
    }

    override suspend fun getAlbumById(id: String): Flow<Resource<AlbumDetail>> = flow {
        emit(Resource.Loading)
        val result = safeApiCall {
            api.getAlbumById(
                token = "Bearer ${token.getAccessToken()}",
                id = id
            ).toAlbumDetail()
        }
        emit(result)
    }

    override suspend fun insertAlbum(album: AlbumEntity) = dao.insertAlbum(album)
    override suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved) = dao.insertSavedAlbum(isAlbumSaved)
    override suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved) = dao.insertListenLaterAlbum(isListenLaterSaved)
    override suspend fun getAlbum(albumId: String): AlbumEntity? = dao.getAlbum(albumId)
    override suspend fun getSavedAlbumState(albumId: String): IsAlbumSaved? = dao.getSavedAlbumState(albumId)
    override suspend fun getListenLaterState(albumId: String): IsListenLaterSaved? = dao.getListenLaterState(albumId)
    override suspend fun getAlbumWithStates(albumId: String): AlbumWithStates? = dao.getAlbumWithStates(albumId)
    override suspend fun deleteAlbum(albumId: String) = dao.deleteAlbum(albumId)
    override suspend fun deleteSavedAlbumState(albumId: String) = dao.deleteSavedAlbumState(albumId)
    override suspend fun deleteListenLaterState(albumId: String) = dao.deleteListenLaterState(albumId)
    override suspend fun getRandomAlbum(): AlbumEntity? = dao.getRandomAlbum()

    override fun getAlbumCount(): Flow<Int> = dao.getAlbumCount()
    override fun getTotalTracksCount(): Flow<Int> = dao.getTotalTracksCount()
    override fun getListenLaterCount(): Flow<Int> = dao.getListenLaterCount()
    override fun getAlbumTypeCount(albumType: String): Flow<Int> = dao.getAlbumTypeCount(albumType)

    override fun getSavedAlbums(): Flow<PagingData<AlbumEntity>> = createPager { dao.getSavedAlbums() }
    override fun getListenLaterAlbums(): Flow<PagingData<AlbumEntity>> = createPager { dao.getListenLaterAlbums() }
    override fun getAlbumType(albumType: String): Flow<PagingData<AlbumEntity>> = createPager { dao.getAlbumType(albumType) }
    override fun searchAlbumByName(searchQuery: String): Flow<PagingData<AlbumEntity>> = createPager { dao.searchAlbumsByName(searchQuery) }
    override fun searchAlbumsListenLater(searchQuery: String): Flow<PagingData<AlbumEntity>> = createPager { dao.searchAlbumsListenLater(searchQuery) }
    override fun getAlbumSortedByName(): Flow<PagingData<AlbumEntity>> = createPager { dao.getAlbumSortedByName() }
    override fun getSavedAlbumAsc(): Flow<PagingData<AlbumEntity>> = createPager { dao.getSavedAlbumAsc() }

}
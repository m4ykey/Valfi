package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.m4ykey.core.network.Resource
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.data.mapper.toTrackItem
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumListUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val trackRepository: TrackRepository
) : ViewModel() {

    private var _search = MutableLiveData(AlbumListUiState())
    val albums : LiveData<AlbumListUiState> get() = _search

    private var _newRelease = MutableLiveData(AlbumListUiState())
    val newRelease : LiveData<AlbumListUiState> get() = _newRelease

    private var _detail = MutableLiveData(AlbumDetailUiState())
    val detail : LiveData<AlbumDetailUiState> get() = _detail

    private var _tracks = MutableLiveData(AlbumTrackUiState())
    val tracks : LiveData<AlbumTrackUiState> get() = _tracks

    private var _albumPaging = MutableLiveData<PagingData<AlbumEntity>>(PagingData.empty())
    val albumPaging : LiveData<PagingData<AlbumEntity>> get() = _albumPaging

    private var _searchResult = MutableLiveData<PagingData<AlbumEntity>>(PagingData.empty())
    val searchResult : LiveData<PagingData<AlbumEntity>> get() = _searchResult

    init { getNewReleases() }

    fun searchAlbumByName(albumName : String) {
        launchPaging(
            source = { repository.searchAlbumByName(albumName) },
            onDataCollected = { search -> _albumPaging.value = search }
        )
    }

    fun searchAlbumsListenLater(albumName : String) {
        launchPaging(
            source = { repository.searchAlbumsListenLater(albumName) },
            onDataCollected = { search -> _albumPaging.value = search }
        )
    }

    fun getAlbumType(albumType : String) {
        launchPaging(
            source = { repository.getAlbumType(albumType) },
            onDataCollected = { type -> _albumPaging.value = type }
        )
    }

    fun getAlbumCount() : Flow<Int> = repository.getAlbumCount()

    fun getTotalTracksCount() : Flow<Int> = repository.getTotalTracksCount()

    fun getListenLaterCount() : Flow<Int> = repository.getListenLaterCount()

    fun getAlbumTypeCount(albumType : String) : Flow<Int> =repository.getAlbumTypeCount(albumType)

    suspend fun getRandomAlbum() : AlbumEntity? = repository.getRandomAlbum()

    fun getSavedAlbums() {
        launchPaging(
            source = { repository.getSavedAlbums() },
            onDataCollected = { album -> _albumPaging.value = album }
        )
    }

    fun getSavedAlbumDesc() {
        launchPaging(
            source = { repository.getSavedAlbumDesc() },
            onDataCollected = { album -> _albumPaging.value = album }
        )
    }

    fun getAlbumSortedByName() {
        launchPaging(
            source = { repository.getAlbumSortedByName() },
            onDataCollected = { sort -> _albumPaging.value = sort }
        )
    }

    fun getListenLaterAlbums() {
        launchPaging(
            source = { repository.getListenLaterAlbums() },
            onDataCollected = { later -> _albumPaging.value = later }
        )
    }

    suspend fun insertAlbum(album : AlbumEntity) = repository.insertAlbum(album)

    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved) = repository.insertSavedAlbum(isAlbumSaved)

    suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved) = repository.insertListenLaterAlbum(isListenLaterSaved)

    suspend fun getAlbum(albumId : String) : AlbumEntity? = repository.getAlbum(albumId)

    suspend fun getSavedAlbumState(albumId : String) : IsAlbumSaved? = repository.getSavedAlbumState(albumId)

    suspend fun getListenLaterState(albumId: String) : IsListenLaterSaved? = repository.getListenLaterState(albumId)

    suspend fun getAlbumWithStates(albumId : String) : AlbumWithStates? = repository.getAlbumWithStates(albumId)

    suspend fun deleteAlbum(albumId: String) = repository.deleteAlbum(albumId)

    suspend fun deleteSavedAlbumState(albumId : String) = repository.deleteSavedAlbumState(albumId)

    suspend fun deleteListenLaterState(albumId: String) = repository.deleteListenLaterState(albumId)

    suspend fun getAlbumById(id : String) {
        repository.getAlbumById(id).collect { resource ->
            _detail.value = resource.toUiState()
        }
    }

    private fun getNewReleases() {
        _newRelease.value = AlbumListUiState(isLoading = true)
        launchPaging(
            source = { repository.getNewReleases() },
            onDataCollected = { pagingData: PagingData<AlbumItem> ->
                val newState = AlbumListUiState(albumList = pagingData)
                _newRelease.value = newState
            }
        )
    }

    fun searchAlbums(query : String) {
        _search.value = AlbumListUiState(isLoading = true)
        launchPaging(
            source = { repository.searchAlbums(query) },
            onDataCollected = { pagingData: PagingData<AlbumItem> ->
                val newState = AlbumListUiState(albumList = pagingData)
                _search.value = newState
            }
        )
    }

    fun getAlbumTracks(id : String) {
        _tracks.value = AlbumTrackUiState(isLoading = true)
        viewModelScope.launch {
            trackRepository.getAlbumTracks(id)
                .cachedIn(viewModelScope)
                .map { it.map(TrackEntity::toTrackItem) }
                .map { Resource.Success(it) }
                .collectResult { _tracks.value = it.toUiState() }
        }
    }

    private fun Resource<AlbumDetail>.toUiState() : AlbumDetailUiState {
        return when (this) {
            is Resource.Success -> AlbumDetailUiState(albumDetail = this.data)
            is Resource.Loading -> AlbumDetailUiState(isLoading = true)
            is Resource.Error -> AlbumDetailUiState(error = this.message)
        }
    }

    private fun Resource<PagingData<TrackItem>>.toUiState() : AlbumTrackUiState {
        return when (this) {
            is Resource.Success -> AlbumTrackUiState(albumTracks = this.data)
            is Resource.Loading -> AlbumTrackUiState(isLoading = true)
            is Resource.Error -> AlbumTrackUiState(error = this.message)
        }
    }

    private fun <T: Any> launchPaging(
        source : () -> Flow<PagingData<T>>,
        onDataCollected : (PagingData<T>) -> Unit
    ) {
        viewModelScope.launch {
            source()
                .cachedIn(this)
                .collect { pagingData ->
                    onDataCollected(pagingData)
                }
        }
    }

    private inline fun <T : Any> Flow<Resource<PagingData<T>>>.collectResult(
        crossinline onResult: (Resource<PagingData<T>>) -> Unit
    ) {
        viewModelScope.launch {
            this@collectResult.collect { resource ->
                onResult(resource)
            }
        }
    }

}
package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.Resource
import com.m4ykey.core.paging.launchPaging
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumListUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val trackRepository: TrackRepository
) : ViewModel() {

    private var _search = MutableLiveData<List<AlbumItem>>()
    val albums : LiveData<List<AlbumItem>> get() = _search

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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> get() = _isError

    private var offset = 0
    var isPaginationEnded = false

    suspend fun searchAlbums(query : String) {
        if (_isLoading.value == true || isPaginationEnded) return

        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                repository.searchAlbums(query, offset = offset, limit = PAGE_SIZE)
                    .collect { searchResult ->
                        if (searchResult.isNotEmpty()) {
                            val currentList = _search.value?.toMutableList() ?: mutableListOf()
                            currentList.addAll(searchResult)
                            _search.value = currentList
                            offset += PAGE_SIZE
                            if (searchResult.size < PAGE_SIZE) {
                                isPaginationEnded = true
                            }
                        } else {
                            isPaginationEnded = true
                        }
                    }
            } catch (e : Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSearch() {
        offset = 0
        _isLoading.value = false
        _isError.value = false
        isPaginationEnded = false
        _search.value = emptyList()
    }

    fun searchAlbumByName(albumName : String) {
        launchPaging(
            scope = viewModelScope,
            source = { repository.searchAlbumByName(albumName) },
            onDataCollected = { search -> _albumPaging.value = search }
        )
    }

    fun searchAlbumsListenLater(albumName : String) {
        launchPaging(
            scope = viewModelScope,
            source = { repository.searchAlbumsListenLater(albumName) },
            onDataCollected = { search -> _albumPaging.value = search }
        )
    }

    fun getAlbumType(albumType : String) {
        launchPaging(
            scope = viewModelScope,
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
            scope = viewModelScope,
            source = { repository.getSavedAlbums() },
            onDataCollected = { album -> _albumPaging.value = album }
        )
    }

    fun getSavedAlbumAsc() {
        launchPaging(
            scope = viewModelScope,
            source = { repository.getSavedAlbumAsc() },
            onDataCollected = { album -> _albumPaging.value = album }
        )
    }

    fun getAlbumSortedByName() {
        launchPaging(
            scope = viewModelScope,
            source = { repository.getAlbumSortedByName() },
            onDataCollected = { sort -> _albumPaging.value = sort }
        )
    }

    fun getListenLaterAlbums() {
        launchPaging(
            scope = viewModelScope,
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

    fun getNewReleases() {
        _newRelease.value = AlbumListUiState(isLoading = true)
        launchPaging(
            scope = viewModelScope,
            source = { repository.getNewReleases() },
            onDataCollected = { pagingData: PagingData<AlbumItem> ->
                val newState = AlbumListUiState(albumList = pagingData)
                _newRelease.value = newState
            }
        )
    }

    fun getAlbumTracks(id : String) {
        _tracks.value = AlbumTrackUiState(isLoading = true)
        launchPaging(
            scope = viewModelScope,
            source = { trackRepository.getAlbumTracks(id) },
            onDataCollected = { pagingData : PagingData<TrackItem> ->
                val newState = AlbumTrackUiState(albumTracks = pagingData)
                _tracks.value = newState
            }
        )
    }

    private fun Resource<AlbumDetail>.toUiState() : AlbumDetailUiState {
        return when (this) {
            is Resource.Success -> AlbumDetailUiState(albumDetail = this.data)
            is Resource.Loading -> AlbumDetailUiState(isLoading = true)
            is Resource.Error -> AlbumDetailUiState(error = this.message)
        }
    }
}
package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.core.network.Resource
import com.m4ykey.core.sort.PreferencesManager
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ListenLaterEntity
import com.m4ykey.data.mapper.toListenLater
import com.m4ykey.ui.adapter.AlbumEntityPagingAdapter
import com.m4ykey.ui.helpers.ListSortingType
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumSearchUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private var _albums = MutableLiveData<AlbumSearchUiState>()
    val albums : LiveData<AlbumSearchUiState> get() = _albums

    private var _detail = MutableLiveData<AlbumDetailUiState>()
    val detail : LiveData<AlbumDetailUiState> get() = _detail

    private var _tracks = MutableLiveData<AlbumTrackUiState>()
    val tracks : LiveData<AlbumTrackUiState> get() = _tracks

    private var _albumPagingData = MutableLiveData<PagingData<AlbumEntity>>()
    val albumPagingData: LiveData<PagingData<AlbumEntity>> = _albumPagingData

    private var _listenLaterPagingData = MutableLiveData<PagingData<ListenLaterEntity>>()
    val listenLaterPagingData : LiveData<PagingData<ListenLaterEntity>> = _listenLaterPagingData

    private var _currentViewType = MutableLiveData(AlbumEntityPagingAdapter.ViewType.GRID)
    val currentViewType : LiveData<AlbumEntityPagingAdapter.ViewType> = _currentViewType

    private var _currentIconResId = MutableLiveData<Int>()
    val currentIconResId : LiveData<Int> get() = _currentIconResId

    private var _localAlbum = MutableLiveData<AlbumEntity>()
    val localAlbum : LiveData<AlbumEntity> get() = _localAlbum

    private var currentSortingType : ListSortingType = ListSortingType.RECENTLY_ADDED

    init {
        viewModelScope.launch {
            preferencesManager.getRecyclerViewViewType().collect { (viewType, iconRes) ->
                val albumViewType = AlbumEntityPagingAdapter.ViewType.entries.toTypedArray().getOrElse(viewType) {
                    AlbumEntityPagingAdapter.ViewType.GRID
                }
                updateViewType(albumViewType, iconRes)
            }
        }
        getAllAlbumsPaged()
    }

    fun getAlbumsOfTypeCompilationPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeCompilationPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.postValue(pagingData)
            }
        }
    }

    fun getAlbumsOfTypeAlbumPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeAlbumPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.postValue(pagingData)
            }
        }
    }

    fun getAlbumsOfTypeEPPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeEPPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.postValue(pagingData)
            }
        }
    }

    fun getAlbumsOfTypeSinglePaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeSinglePaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.postValue(pagingData)
            }
        }
    }

    fun getAllAlbumsPaged() {
        viewModelScope.launch {
            when (currentSortingType) {
                ListSortingType.ALPHABETICAL -> {
                    repository.getAlbumSortedAlphabetical().cachedIn(viewModelScope).collect { pagingData ->
                        _albumPagingData.postValue(pagingData)
                    }
                }
                ListSortingType.RECENTLY_ADDED -> {
                    repository.getAllAlbumsPaged().cachedIn(viewModelScope).collect { pagingData ->
                        _albumPagingData.postValue(pagingData)
                    }
                }
            }
        }
    }

    fun saveRecyclerViewType(newViewType : AlbumEntityPagingAdapter.ViewType, iconResId: Int) {
        viewModelScope.launch {
            preferencesManager.setRecyclerViewType(newViewType.ordinal, iconResId)
        }
    }

    private fun updateViewType(viewType : AlbumEntityPagingAdapter.ViewType, iconResId : Int) {
        _currentViewType.value = viewType
        _currentIconResId.value = iconResId
    }

    fun updateSortingType(sortingType: ListSortingType) {
        viewModelScope.launch {
            currentSortingType = sortingType
            getAllAlbumsPaged()
        }
    }

    suspend fun getLocalAlbumById(albumId : String) {
        withContext(Dispatchers.IO) {
            val album = repository.getLocalAlbumById(albumId)
            _localAlbum.postValue(album)
        }
    }

    suspend fun saveAlbum(album : AlbumEntity, isListenLater : Boolean = false) {
        viewModelScope.launch {
            repository.saveAlbum(album)
            if (isListenLater) repository.saveListenLater(album.toListenLater())
        }
    }

    suspend fun deleteAlbum(album: AlbumEntity, isListenLater: Boolean = false) {
        viewModelScope.launch {
            repository.deleteAlbum(album)
            if (isListenLater) repository.deleteListenLater(album.toListenLater())
        }
    }

    suspend fun getAlbumById(id : String) {
        repository.getAlbumById(id).onEach { result ->
            when (result) {
                is Resource.Loading -> { _detail.value = AlbumDetailUiState(true, null, null) }
                is Resource.Success -> { _detail.value = AlbumDetailUiState(false, null, result.data) }
                is Resource.Error -> { _detail.value = AlbumDetailUiState(false, result.message ?: "Unknown message", null) }
            }
        }.launchIn(viewModelScope)
    }

    fun searchAlbums(query : String) {
        viewModelScope.launch {
            _albums.value = AlbumSearchUiState(isLoading = true)
            try {
                repository.searchAlbums(query).cachedIn(viewModelScope).collectLatest { albums ->
                    _albums.value = AlbumSearchUiState(albumSearch = albums)
                }
            } catch (e : Exception) {
                _albums.value = AlbumSearchUiState(error = e.message ?: "Unknown error")
            } finally {
                _albums.value = AlbumSearchUiState(isLoading = false)
            }
        }
    }

    fun getAlbumTracks(id : String) {
        viewModelScope.launch {
            _tracks.value = AlbumTrackUiState(isLoading = true)
            try {
                repository.getAlbumTracks(id).cachedIn(viewModelScope).collectLatest { tracks ->
                    _tracks.value = AlbumTrackUiState(albumTracks = tracks)
                }
            } catch (e : Exception) {
                _tracks.value = AlbumTrackUiState(error = e.message ?: "Unknown error")
            } finally {
                _tracks.value = AlbumTrackUiState(isLoading = false)
            }
        }
    }
}
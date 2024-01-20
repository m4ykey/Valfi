package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.core.network.Resource
import com.m4ykey.data.domain.model.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.ui.uistate.AlbumDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private var searchQuery : String = ""
    private var _albums = MutableLiveData<PagingData<AlbumItem>>()
    val albums : LiveData<PagingData<AlbumItem>> get() = _albums

    private var _detail = MutableLiveData<AlbumDetailUiState>()
    val detail : LiveData<AlbumDetailUiState> get() = _detail

    suspend fun getAlbumById(id : String) {
        repository.getAlbumById(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _detail.value = detail.value?.copy(
                        isLoading = true,
                        albumDetail = result.data!!
                    )
                }
                is Resource.Success -> {
                    _detail.value = detail.value?.copy(
                        isLoading = false,
                        albumDetail = result.data!!
                    )
                }
                is Resource.Error -> {
                    _detail.value = detail.value?.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error",
                        albumDetail = result.data!!
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun searchAlbums(query : String) {
        searchQuery = query
        viewModelScope.launch {
            repository.searchAlbums(query).cachedIn(viewModelScope).collectLatest { albums ->
                _albums.value = albums
            }
        }
    }
}
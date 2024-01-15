package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.data.domain.model.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private var searchQuery : String = ""
    private var _albums = MutableLiveData<PagingData<AlbumItem>>()
    val albums : LiveData<PagingData<AlbumItem>> get() = _albums

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private fun setLoading(isLoading : Boolean) {
        _isLoading.value = isLoading
    }

    fun searchAlbums(query : String) {
        searchQuery = query
        viewModelScope.launch {
            try {
                setLoading(true)
                repository.searchAlbums(query).cachedIn(viewModelScope).collectLatest { albums ->
                    _albums.value = albums
                }
            } finally {
                setLoading(false)
            }
        }
    }
}
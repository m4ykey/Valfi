package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
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

    fun searchAlbums(query : String) {
        searchQuery = query
        viewModelScope.launch {
            repository.searchAlbums(query).collectLatest { albums ->
                _albums.value = albums
            }
        }
    }

}
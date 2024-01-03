package com.m4ykey.valfi2.album.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.valfi2.album.data.domain.model.Item
import com.m4ykey.valfi2.album.data.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

class AlbumViewModel(
    repository: AlbumRepository
) : ViewModel() {

    var albums : Flow<PagingData<Item>> = repository.searchAlbums().cachedIn(viewModelScope)

}
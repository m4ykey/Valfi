package com.example.vuey.feature_album.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_album.data.local.source.AlbumLocalDataSource
import com.example.vuey.feature_album.data.local.source.entity.AlbumEntity
import com.example.vuey.feature_album.domain.repository.AlbumRepository
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.DetailAlbumUiState
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.SearchAlbumUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val dataSource: AlbumLocalDataSource
) : ViewModel() {

    private val _albumSearchUiState = MutableStateFlow<SearchAlbumUiState>(SearchAlbumUiState.Loading)
    val albumSearchUiState: StateFlow<SearchAlbumUiState> = _albumSearchUiState

    private val _albumDetailUiState = MutableStateFlow<DetailAlbumUiState>(DetailAlbumUiState.Loading)
    val albumDetailUiState: StateFlow<DetailAlbumUiState> = _albumDetailUiState

    private val _searchAlbumInDatabase = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val searchAlbumInDatabase: StateFlow<List<AlbumEntity>> = _searchAlbumInDatabase

    fun getTotalTracks(): Flow<Int> {
        return dataSource.getTotalTracks()
    }

    fun getTotalLength(): Flow<Int> {
        return dataSource.getTotalLength()
    }

    fun getAlbumCount(): Flow<Int> {
        return dataSource.getAlbumCount()
    }

    val allAlbums = dataSource.getAllAlbums()

    suspend fun refreshDetail(albumId: String) {
        getAlbumDetail(albumId)
    }

    fun insertAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            dataSource.insertAlbum(albumEntity)
        }
    }

    fun deleteAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            dataSource.deleteAlbum(albumEntity)
        }
    }

    fun getAlbumById(albumId: String): Flow<AlbumEntity> {
        return dataSource.getAlbumById(albumId)
    }

    suspend fun getAlbumDetail(albumId: String) {
        repository.getAlbum(albumId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _albumDetailUiState.tryEmit(
                        DetailAlbumUiState.Failure(
                            message = result.message ?: "Unknown error"
                        )
                    )
                }
                is Resource.Loading -> {
                    _albumDetailUiState.tryEmit(DetailAlbumUiState.Loading)
                }
                is Resource.Success -> {
                    _albumDetailUiState.tryEmit(
                        DetailAlbumUiState.Success(
                            albumData = result.data!!
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun searchAlbum(albumName: String) {
        repository.searchAlbum(albumName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _albumSearchUiState.tryEmit(
                        SearchAlbumUiState.Success(
                            albumData = result.data ?: emptyList()
                        )
                    )
                }

                is Resource.Failure -> {
                   _albumSearchUiState.tryEmit(
                       SearchAlbumUiState.Failure(
                           message = result.message ?: "Unknown error"
                       )
                   )
                }

                is Resource.Loading -> {
                    _albumSearchUiState.tryEmit(SearchAlbumUiState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }
}
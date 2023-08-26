package com.example.vuey.presentation.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.presentation.album.viewmodel.ui_state.DetailAlbumUiState
import com.example.vuey.presentation.album.viewmodel.ui_state.SearchAlbumUiState
import com.m4ykey.common.network.Resource
import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
import com.m4ykey.repository.album.AlbumLocalRepository
import com.m4ykey.repository.album.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val repositoryLocal: AlbumLocalRepository
) : ViewModel() {

    private val _albumSearchUiState = MutableStateFlow<SearchAlbumUiState>(SearchAlbumUiState.Loading)
    val albumSearchUiState = _albumSearchUiState.asStateFlow()

    private val _albumDetailUiState = MutableStateFlow<DetailAlbumUiState>(DetailAlbumUiState.Loading)
    val albumDetailUiState = _albumDetailUiState.asStateFlow()

    val allAlbums = repositoryLocal.getAllAlbums()
    val allListenLaterAlbums = repositoryLocal.getAllListenLaterAlbums()

    fun insertAlbumToListenLater(listenLaterEntity: ListenLaterEntity) {
        viewModelScope.launch {
            repositoryLocal.insertAlbumToListenLater(listenLaterEntity)
        }
    }

    fun deleteAlbumToListenLater(listenLaterEntity: ListenLaterEntity) {
        viewModelScope.launch {
            repositoryLocal.deleteAlbumToListenLater(listenLaterEntity)
        }
    }

    fun getListenLaterAlbumById(albumId: String): Flow<ListenLaterEntity> = repositoryLocal.getListenLaterAlbumById(albumId)

    fun getTotalTracks(): Flow<Int> = repositoryLocal.getTotalTracks()

    fun getTotalLength(): Flow<Int> = repositoryLocal.getTotalLength()

    fun getAlbumCount(): Flow<Int> = repositoryLocal.getAlbumCount()

    fun insertAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            repositoryLocal.insertAlbum(albumEntity)
        }
    }

    fun deleteAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            repositoryLocal.deleteAlbum(albumEntity)
        }
    }

    fun getAlbumById(albumId: String): Flow<AlbumEntity> = repositoryLocal.getAlbumById(albumId)

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
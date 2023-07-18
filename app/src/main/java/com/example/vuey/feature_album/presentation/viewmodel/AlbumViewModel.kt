package com.example.vuey.feature_album.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.domain.repository.AlbumRepository
import com.example.vuey.feature_album.domain.use_cases.AlbumUseCases
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.DetailAlbumUiState
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.SearchAlbumUiState
import com.example.vuey.util.network.Resource
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
    private val useCases: AlbumUseCases,
    private val repository: AlbumRepository
) : ViewModel() {

    private val _albumSearchUiState = MutableStateFlow(SearchAlbumUiState())
    val albumSearchUiState: StateFlow<SearchAlbumUiState> = _albumSearchUiState

    private val _albumDetailUiState = MutableStateFlow(DetailAlbumUiState())
    val albumDetailUiState: StateFlow<DetailAlbumUiState> = _albumDetailUiState

    private val _searchAlbumInDatabase = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val searchAlbumInDatabase: StateFlow<List<AlbumEntity>> = _searchAlbumInDatabase

    fun getTotalTracks(): Flow<Int> {
        return repository.getTotalTracks()
    }

    fun getTotalLength(): Flow<Int> {
        return repository.getTotalLength()
    }

    fun getAlbumCount(): Flow<Int> {
        return repository.getAlbumCount()
    }

    fun searchAlbumDatabase(albumName: String) {
        viewModelScope.launch {
            repository.searchAlbumInDatabase(albumName).collect { albumList ->
                _searchAlbumInDatabase.emit(albumList)
            }
        }
    }

    val allAlbums = repository.getAllAlbums()

    suspend fun refreshDetail(albumId: String) {
        getAlbumDetail(albumId)
    }

    fun insertAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            repository.insertAlbum(albumEntity)
        }
    }

    fun deleteAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            repository.deleteAlbum(albumEntity)
        }
    }

    fun getAlbumById(albumId: String): Flow<AlbumEntity> {
        return repository.getAlbumById(albumId)
    }

    suspend fun getAlbumDetail(albumId: String) {
        useCases.getAlbumDetailUseCase(albumId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _albumDetailUiState.value = albumDetailUiState.value.copy(
                        detailAlbumData = result.data,
                        isLoading = false
                    )
                }

                is Resource.Failure -> {
                    _albumDetailUiState.value = albumDetailUiState.value.copy(
                        detailAlbumData = result.data,
                        isLoading = false,
                        isError = result.message ?: "Unknown error"
                    )
                }

                is Resource.Loading -> {
                    _albumDetailUiState.value = albumDetailUiState.value.copy(
                        detailAlbumData = result.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun searchAlbum(albumName: String) {
        useCases.getAlbumSearchUseCase(albumName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _albumSearchUiState.value = albumSearchUiState.value.copy(
                        searchAlbumData = result.data ?: emptyList(),
                        isLoading = false
                    )
                }

                is Resource.Failure -> {
                    _albumSearchUiState.value = albumSearchUiState.value.copy(
                        searchAlbumData = result.data ?: emptyList(),
                        isLoading = false,
                        isError = result.message ?: "Unknown error"
                    )
                }

                is Resource.Loading -> {
                    _albumSearchUiState.value = albumSearchUiState.value.copy(
                        searchAlbumData = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
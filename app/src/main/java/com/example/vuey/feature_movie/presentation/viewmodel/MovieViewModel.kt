package com.example.vuey.feature_movie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.domain.repository.MovieRepository
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.CastMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.DetailMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.SearchMovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieSearchUiState = MutableStateFlow<SearchMovieUiState>(SearchMovieUiState.Loading)
    val movieSearchUiState : StateFlow<SearchMovieUiState> = _movieSearchUiState

    private val _movieDetailUiState = MutableStateFlow<DetailMovieUiState>(DetailMovieUiState.Loading)
    val movieDetailUiState : StateFlow<DetailMovieUiState> = _movieDetailUiState

    private val _movieCastUiState = MutableStateFlow<CastMovieUiState>(CastMovieUiState.Loading)
    val movieCastUiState : StateFlow<CastMovieUiState> = _movieCastUiState

    private val _searchMovieInDatabase = MutableStateFlow<List<MovieEntity>>(emptyList())
    val searchMovieInDatabase : StateFlow<List<MovieEntity>> = _searchMovieInDatabase

    val allMovies = repository.getAllMovies()

    fun getTotalLength() : Flow<Int> {
        return repository.getTotalLength()
    }

    fun getMovieCount() : Flow<Int> {
        return repository.getMovieCount()
    }

    fun getMovieById(movieId: Int) : Flow<MovieEntity> {
        return repository.getMovieById(movieId)
    }

    suspend fun refreshDetail(movieId : Int) {
        getMovieDetail(movieId)
    }

    fun insertMovie(movieEntity: MovieEntity) {
        viewModelScope.launch {
            repository.insertMovie(movieEntity)
        }
    }

    fun deleteMovie(movieEntity: MovieEntity) {
        viewModelScope.launch {
            repository.deleteMovie(movieEntity)
        }
    }

    suspend fun getMovieCast(movieId: Int) {
        repository.getMovieCast(movieId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieCastUiState.tryEmit(
                        CastMovieUiState.Failure(
                            message = result.message ?: "Unknown error"
                        )
                    )
                }
                is Resource.Success -> {
                    _movieCastUiState.tryEmit(
                        CastMovieUiState.Success(
                            castData = result.data ?: emptyList()
                        )
                    )
                }
                is Resource.Loading -> {
                    _movieCastUiState.tryEmit(CastMovieUiState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getMovieDetail(movieId : Int) {
        repository.getMovieDetail(movieId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieDetailUiState.tryEmit(
                        DetailMovieUiState.Failure(
                            message = result.message ?: "Unknown error"
                        )
                    )
                }
                is Resource.Loading -> {
                    _movieDetailUiState.tryEmit(DetailMovieUiState.Loading)
                }
                is Resource.Success -> {
                    _movieDetailUiState.tryEmit(
                        DetailMovieUiState.Success(
                            movieData = result.data!!
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun searchMovie(movieName : String) {
        repository.searchMovie(movieName).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieSearchUiState.tryEmit(
                        SearchMovieUiState.Failure(
                            message = result.message ?: "Unknown error"
                        )
                    )
                }
                is Resource.Success -> {
                    _movieSearchUiState.tryEmit(
                        SearchMovieUiState.Success(
                            movieData = result.data ?: emptyList()
                        )
                    )
                }
                is Resource.Loading -> {
                    _movieSearchUiState.tryEmit(SearchMovieUiState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }
}
package com.example.vuey.feature_movie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.repository.MovieRepository
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.CastMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.DetailMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.SearchMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieUseCases
import com.example.vuey.core.common.network.Resource
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
    private val useCase : MovieUseCases,
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieSearchUiState = MutableStateFlow(SearchMovieUiState())
    val movieSearchUiState : StateFlow<SearchMovieUiState> = _movieSearchUiState

    private val _movieDetailUiState = MutableStateFlow(DetailMovieUiState())
    val movieDetailUiState : StateFlow<DetailMovieUiState> = _movieDetailUiState

    private val _movieCastUiState = MutableStateFlow(CastMovieUiState())
    val movieCastUiState : StateFlow<CastMovieUiState> = _movieCastUiState

    private val _searchMovieInDatabase = MutableStateFlow<List<MovieEntity>>(emptyList())
    val searchMovieInDatabase : StateFlow<List<MovieEntity>> = _searchMovieInDatabase

    val allMovies = repository.getAllMovies()

    fun searchMovieDatabase(movieTitle : String) {
        viewModelScope.launch {
            repository.searchMovieInDatabase(movieTitle).collect { movieList ->
                _searchMovieInDatabase.emit(movieList)
            }
        }
    }

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
        useCase.getMovieCastUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieCastUiState.value = movieCastUiState.value.copy(
                        castMovieData = result.data ?: emptyList(),
                        isLoading = false,
                        isError = result.message ?: "Unknown error"
                    )
                }
                is Resource.Success -> {
                    _movieCastUiState.value = movieCastUiState.value.copy(
                        castMovieData = result.data ?: emptyList(),
                        isLoading = false,
                    )
                }
                is Resource.Loading -> {
                    _movieCastUiState.value = movieCastUiState.value.copy(
                        castMovieData = result.data ?: emptyList(),
                        isLoading = true,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getMovieDetail(movieId : Int) {
        useCase.getMovieDetailUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieDetailUiState.value = movieDetailUiState.value.copy(
                        detailMovieData = result.data,
                        isLoading = false,
                        isError = result.message ?: "Unknown error"
                    )
                }
                is Resource.Loading -> {
                    _movieDetailUiState.value = movieDetailUiState.value.copy(
                        detailMovieData = result.data,
                        isLoading = true,
                    )
                }
                is Resource.Success -> {
                    _movieDetailUiState.value = movieDetailUiState.value.copy(
                        detailMovieData = result.data,
                        isLoading = false,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun searchMovie(movieName : String) {
        useCase.getMovieSearchUseCase(movieName).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieSearchUiState.value = movieSearchUiState.value.copy(
                        isLoading = false,
                        isError = result.message ?: "Unknown error",
                        searchMovieData = result.data ?: emptyList()
                    )
                }
                is Resource.Success -> {
                    _movieSearchUiState.value = movieSearchUiState.value.copy(
                        isLoading = false,
                        searchMovieData = result.data ?: emptyList()
                    )
                }
                is Resource.Loading -> {
                    _movieSearchUiState.value = movieSearchUiState.value.copy(
                        isLoading = true,
                        searchMovieData = result.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
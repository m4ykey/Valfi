package com.example.vuey.feature_movie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_movie.data.local.source.MovieLocalDataSource
import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import com.example.vuey.feature_movie.data.local.source.entity.WatchLaterEntity
import com.example.vuey.feature_movie.domain.repository.MovieRepository
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.CastMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.DetailMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.SearchMovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val dataSource: MovieLocalDataSource
) : ViewModel() {

    private val _movieSearchUiState = MutableStateFlow<SearchMovieUiState>(SearchMovieUiState.Loading)
    val movieSearchUiState = _movieSearchUiState.asStateFlow()

    private val _movieDetailUiState = MutableStateFlow<DetailMovieUiState>(DetailMovieUiState.Loading)
    val movieDetailUiState = _movieDetailUiState.asStateFlow()

    private val _movieCastUiState = MutableStateFlow<CastMovieUiState>(CastMovieUiState.Loading)
    val movieCastUiState = _movieCastUiState.asStateFlow()

    val allMovies = dataSource.getAllMovies()
    val allWatchLaterMovies = dataSource.getAllWatchLaterMovies()

    fun insertWatchLaterMovie(watchLaterEntity: WatchLaterEntity) {
        viewModelScope.launch {
            dataSource.insertWatchLaterMovie(watchLaterEntity)
        }
    }

    fun deleteWatchLaterMovie(watchLaterEntity: WatchLaterEntity) {
        viewModelScope.launch {
            dataSource.deleteWatchLaterMovie(watchLaterEntity)
        }
    }

    fun getWatchLaterMovieById(movieId: Int) : Flow<WatchLaterEntity> {
        return dataSource.getWatchLaterMovieById(movieId)
    }

    fun getTotalLength() : Flow<Int> {
        return dataSource.getTotalLength()
    }

    fun getMovieCount() : Flow<Int> {
        return dataSource.getMovieCount()
    }

    fun getMovieById(movieId: Int) : Flow<MovieEntity> {
        return dataSource.getMovieById(movieId)
    }

    fun insertMovie(movieEntity: MovieEntity) {
        viewModelScope.launch {
            dataSource.insertMovie(movieEntity)
        }
    }

    fun deleteMovie(movieEntity: MovieEntity) {
        viewModelScope.launch {
            dataSource.deleteMovie(movieEntity)
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
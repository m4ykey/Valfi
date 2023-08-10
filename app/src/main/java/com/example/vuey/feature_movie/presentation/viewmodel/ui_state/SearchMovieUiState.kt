package com.example.vuey.feature_movie.presentation.viewmodel.ui_state

import androidx.paging.PagingData
import com.example.vuey.feature_movie.data.remote.model.MovieList
import kotlinx.coroutines.flow.Flow

sealed class SearchMovieUiState {
    data object Loading : SearchMovieUiState()
    data class Failure(val message: String) : SearchMovieUiState()
    data class Success(val movieData: Flow<PagingData<MovieList>>) : SearchMovieUiState()
}
package com.example.vuey.feature_movie.presentation.viewmodel.ui_state

import com.example.vuey.feature_movie.data.remote.model.MovieList

sealed class SearchMovieUiState {
    data object Loading : SearchMovieUiState()
    data class Failure(val message: String) : SearchMovieUiState()
    data class Success(val movieData : List<MovieList>) : SearchMovieUiState()
}
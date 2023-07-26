package com.example.vuey.feature_movie.presentation.viewmodel.ui_state

import com.example.vuey.feature_movie.data.remote.model.MovieDetail

sealed class DetailMovieUiState {
    data object Loading : DetailMovieUiState()
    data class Failure(val message : String) : DetailMovieUiState()
    data class Success(val movieData : MovieDetail) : DetailMovieUiState()
}
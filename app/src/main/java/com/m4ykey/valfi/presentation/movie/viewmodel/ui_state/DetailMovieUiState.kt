package com.m4ykey.valfi.presentation.movie.viewmodel.ui_state

import com.m4ykey.remote.movie.model.MovieDetail

sealed class DetailMovieUiState {
    data object Loading : DetailMovieUiState()
    data class Failure(val message : String) : DetailMovieUiState()
    data class Success(val movieData : MovieDetail) : DetailMovieUiState()
}
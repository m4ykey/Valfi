package com.m4ykey.valfi.presentation.movie.viewmodel.ui_state

import com.m4ykey.remote.movie.model.MovieList

sealed class SearchMovieUiState {
    data object Loading : SearchMovieUiState()
    data class Failure(val message: String) : SearchMovieUiState()
    data class Success(val movieData: List<MovieList>) : SearchMovieUiState()
}
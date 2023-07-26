package com.example.vuey.feature_movie.presentation.viewmodel.ui_state

import com.example.vuey.feature_movie.data.remote.model.MovieCast

sealed class CastMovieUiState {
    data object Loading : CastMovieUiState()
    data class Failure(val message : String) : CastMovieUiState()
    data class Success(val castData : List<MovieCast.CastDetail>) : CastMovieUiState()
}

package com.m4ykey.valfi.presentation.movie.viewmodel.ui_state

import com.m4ykey.remote.movie.model.MovieCast

sealed class CastMovieUiState {
    data object Loading : CastMovieUiState()
    data class Failure(val message : String) : CastMovieUiState()
    data class Success(val castData : List<MovieCast.CastDetail>) : CastMovieUiState()
}

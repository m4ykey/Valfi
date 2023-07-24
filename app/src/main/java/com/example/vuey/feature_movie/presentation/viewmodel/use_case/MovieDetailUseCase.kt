package com.example.vuey.feature_movie.presentation.viewmodel.use_case

import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.repository.MovieRepository
import com.example.vuey.core.common.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId : Int) : Flow<Resource<MovieDetail>> {
        return repository.getMovieDetail(movieId)
    }
}

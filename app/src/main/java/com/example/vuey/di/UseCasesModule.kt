package com.example.vuey.di

import com.example.vuey.feature_album.domain.use_cases.AlbumDetailUseCase
import com.example.vuey.feature_album.domain.use_cases.AlbumSearchUseCase
import com.example.vuey.feature_album.domain.use_cases.AlbumUseCases
import com.example.vuey.feature_artist.domain.usecase.ArtistBioUseCase
import com.example.vuey.feature_artist.domain.usecase.ArtistInfoUseCase
import com.example.vuey.feature_artist.domain.usecase.ArtistTopTracksUseCase
import com.example.vuey.feature_artist.domain.usecase.ArtistUseCase
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieCastUseCase
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieDetailUseCase
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieSearchUseCase
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideAlbumUseCases(
        getAlbumSearchUseCase: AlbumSearchUseCase,
        getAlbumDetailUseCase: AlbumDetailUseCase,
    ): AlbumUseCases {
        return AlbumUseCases(
            getAlbumSearchUseCase,
            getAlbumDetailUseCase,
        )
    }

    @Provides
    fun provideMovieUseCases(
        getMovieSearchUseCase: MovieSearchUseCase,
        getMovieDetailUseCase: MovieDetailUseCase,
        getMovieCastUseCase: MovieCastUseCase
    ): MovieUseCases {
        return MovieUseCases(
            getMovieSearchUseCase,
            getMovieDetailUseCase,
            getMovieCastUseCase
        )
    }

    @Provides
    fun provideArtistUseCase(
        getArtistBioUseCase: ArtistBioUseCase,
        getArtistInfoUseCase: ArtistInfoUseCase,
        getArtistTopTracksUseCase: ArtistTopTracksUseCase
    ): ArtistUseCase {
        return ArtistUseCase(
            getArtistInfoUseCase,
            getArtistBioUseCase,
            getArtistTopTracksUseCase
        )
    }

}
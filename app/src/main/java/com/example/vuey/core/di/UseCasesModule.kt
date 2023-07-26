package com.example.vuey.core.di

import com.example.vuey.feature_artist.domain.usecase.ArtistBioUseCase
import com.example.vuey.feature_artist.domain.usecase.ArtistInfoUseCase
import com.example.vuey.feature_artist.domain.usecase.ArtistTopTracksUseCase
import com.example.vuey.feature_artist.domain.usecase.ArtistUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

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
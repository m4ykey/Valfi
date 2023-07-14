package com.example.vuey.feature_artist.domain.usecase

data class ArtistUseCase(
    val getArtistInfoUseCase : ArtistInfoUseCase,
    val getArtistBioUseCase : ArtistBioUseCase
)

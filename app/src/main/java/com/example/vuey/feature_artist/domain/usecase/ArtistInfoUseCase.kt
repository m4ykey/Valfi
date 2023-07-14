package com.example.vuey.feature_artist.domain.usecase

import com.example.vuey.feature_artist.data.remote.model.spotify.artist.ArtistInfo
import com.example.vuey.feature_artist.domain.repository.ArtistRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistInfoUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    suspend operator fun invoke(artistId : String) : Flow<Resource<ArtistInfo>> {
        return repository.getArtistInfo(artistId)
    }
}
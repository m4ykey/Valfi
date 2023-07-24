package com.example.vuey.feature_artist.domain.usecase

import com.example.vuey.feature_artist.data.remote.model.last_fm.ArtistBio
import com.example.vuey.feature_artist.domain.repository.ArtistRepository
import com.example.vuey.core.common.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistBioUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    suspend operator fun invoke(artistName : String) : Flow<Resource<ArtistBio>> {
        return repository.getArtistBio(artistName)
    }
}
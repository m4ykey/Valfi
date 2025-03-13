package com.m4ykey.data.domain.usecase.artist

import com.m4ykey.data.domain.model.artist.ArtistList
import com.m4ykey.data.domain.repository.ArtistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistUseCase @Inject constructor(
    private val repository : ArtistRepository
) {

    suspend fun getSeveralArtists(ids : String) : Flow<List<ArtistList>> = repository.getSeveralArtists(ids)

}
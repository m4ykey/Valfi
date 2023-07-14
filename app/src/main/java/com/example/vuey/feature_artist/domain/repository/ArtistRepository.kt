package com.example.vuey.feature_artist.domain.repository

import com.example.vuey.feature_artist.data.remote.model.last_fm.ArtistBio
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtistBio(artistName : String) : Flow<Resource<ArtistBio>>

}
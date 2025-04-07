package com.m4ykey.data.domain.repository

import com.m4ykey.data.domain.model.artist.ArtistList
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getSeveralArtists(ids : String) : Flow<List<ArtistList>>

}
package com.m4ykey.data.domain.repository

import com.m4ykey.data.domain.model.album.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getSeveralArtists(ids : String) : Flow<List<Artist>>

}
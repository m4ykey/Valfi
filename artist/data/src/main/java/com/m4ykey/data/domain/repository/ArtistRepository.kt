package com.m4ykey.data.domain.repository

import com.m4ykey.data.domain.model.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtist(id : String) : Flow<Artist>

}
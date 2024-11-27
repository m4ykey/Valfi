package com.m4ykey.data.domain.repository

import com.m4ykey.data.domain.model.Artist
import com.m4ykey.data.domain.model.album.ArtistAlbum
import com.m4ykey.data.domain.model.top_tracks.Track
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtist(id : String) : Flow<Artist>
    suspend fun getArtistTopTracks(id : String) : Flow<List<Track>>
    suspend fun getArtistAlbums(
        id : String,
        limit : Int,
        offset : Int,
        includeGroups : String
    ) : Flow<List<ArtistAlbum>>

}
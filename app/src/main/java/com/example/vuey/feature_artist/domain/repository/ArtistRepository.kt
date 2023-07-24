package com.example.vuey.feature_artist.domain.repository

import com.example.vuey.feature_artist.data.remote.model.last_fm.ArtistBio
import com.example.vuey.feature_artist.data.remote.model.spotify.artist.ArtistInfo
import com.example.vuey.feature_artist.data.remote.model.spotify.top_tracks.Track
import com.example.vuey.core.common.network.Resource
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtistBio(artistName : String) : Flow<Resource<ArtistBio>>
    suspend fun getArtistInfo(artistId : String) : Flow<Resource<ArtistInfo>>
    suspend fun getArtistTopTracks(artistId : String) : Flow<Resource<List<Track>>>

}
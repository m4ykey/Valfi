package com.example.vuey.feature_artist.domain.repository

import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_artist.data.remote.model.spotify.artist.ArtistInfo
import com.example.vuey.feature_artist.data.remote.model.spotify.top_tracks.Track
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtistInfo(artistId : String) : Flow<Resource<ArtistInfo>>
    suspend fun getArtistTopTracks(artistId : String) : Flow<Resource<List<Track>>>

}
package com.example.vuey.feature_artist.data.remote.api

import com.example.vuey.feature_artist.data.remote.model.spotify.artist.ArtistInfo
import com.example.vuey.feature_artist.data.remote.model.spotify.top_tracks.ArtistTopTracks
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface ArtistSpotifyApi {

    @GET("v1/artists/{id}")
    suspend fun getArtistInfo(
        @Path("id") artistId : String,
        @Header("Authorization") token : String
    ) : ArtistInfo

    @GET("v1/artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(
        @Path("id") artistId: String,
        @Query("market") market : String = Locale.getDefault().country,
        @Header("Authorization") token : String
    ) : ArtistTopTracks
}
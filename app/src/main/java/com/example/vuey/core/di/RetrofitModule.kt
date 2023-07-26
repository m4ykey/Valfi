package com.example.vuey.core.di

import com.example.vuey.feature_album.data.remote.api.AlbumApi
import com.example.vuey.feature_album.data.remote.api.AuthApi
import com.example.vuey.feature_artist.data.remote.api.ArtistSpotifyApi
import com.example.vuey.feature_movie.data.remote.api.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideSpotifyApi(@Named("Album") retrofit: Retrofit) : AlbumApi {
        return retrofit.create(AlbumApi::class.java)
    }

    @Provides
    fun provideSpotifyAuth(@Named("Auth") retrofit: Retrofit) : AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun provideMovieApi(@Named("Movie") retrofit: Retrofit) : MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    fun provideSpotifyArtistApi(@Named("Artist") retrofit: Retrofit) : ArtistSpotifyApi {
        return retrofit.create(ArtistSpotifyApi::class.java)
    }
}
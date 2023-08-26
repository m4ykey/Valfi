package com.m4ykey.remote.di

import com.m4ykey.remote.album.api.AlbumApi
import com.m4ykey.remote.album.api.AuthApi
import com.m4ykey.remote.movie.api.MovieApi
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
}
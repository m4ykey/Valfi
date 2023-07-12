package com.example.vuey.di

import com.example.vuey.feature_album.data.remote.api.AlbumApi
import com.example.vuey.feature_album.data.remote.api.AuthApi
import com.example.vuey.feature_album.data.remote.token.LastFmInterceptor
import com.example.vuey.feature_album.data.remote.token.SpotifyInterceptor
import com.example.vuey.feature_movie.data.remote.api.MovieApi
import com.example.vuey.feature_movie.data.remote.token.TmdbInterceptor
import com.example.vuey.util.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTmdbInterceptor() : TmdbInterceptor = TmdbInterceptor()

    @Provides
    @Singleton
    fun provideLastFmInterceptor() : LastFmInterceptor = LastFmInterceptor()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        spotifyInterceptor: SpotifyInterceptor,
        tmdbInterceptor: TmdbInterceptor,
        lastFmInterceptor: LastFmInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tmdbInterceptor)
            .addInterceptor(spotifyInterceptor)
            .addInterceptor(lastFmInterceptor)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAuth(
        gsonConverterFactory: GsonConverterFactory
    ) : AuthApi {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(Constants.SPOTIFY_AUTH_URL)
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAlbumRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : AlbumApi {
        return Retrofit.Builder()
            .baseUrl(Constants.SPOTIFY_BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(AlbumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : MovieApi {
        return Retrofit.Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(MovieApi::class.java)
    }

}
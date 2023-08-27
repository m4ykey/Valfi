package com.m4ykey.remote.di

import com.m4ykey.remote.Urls.SPOTIFY_AUTH_URL
import com.m4ykey.remote.Urls.SPOTIFY_BASE_URL
import com.m4ykey.remote.Urls.TMDB_BASE_URL
import com.m4ykey.remote.album.token.SpotifyInterceptor
import com.m4ykey.remote.movie.token.TmdbInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTmdbInterceptor(): TmdbInterceptor = TmdbInterceptor()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        spotifyInterceptor: SpotifyInterceptor,
        tmdbInterceptor: TmdbInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(tmdbInterceptor)
        .addInterceptor(spotifyInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    @Provides
    @Singleton
    @Named("Auth")
    fun provideSpotifyAuth(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(SPOTIFY_AUTH_URL)
            .build()

    @Provides
    @Singleton
    @Named("Album")
    fun provideAlbumRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .baseUrl(SPOTIFY_BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    @Named("Movie")
    fun provideMovieRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
}
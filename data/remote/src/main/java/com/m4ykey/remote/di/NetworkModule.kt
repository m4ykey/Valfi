package com.m4ykey.remote.di

import com.m4ykey.remote.album.token.SpotifyInterceptor
import com.m4ykey.remote.movie.interceptor.TmdbInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideMoshi() : Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

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
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    @Provides
    @Singleton
    @Named("Auth")
    fun provideSpotifyAuth(): Retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://accounts.spotify.com/")
            .build()

    @Provides
    @Singleton
    @Named("Album")
    fun provideAlbumRetrofit(
        httpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("Movie")
    fun provideMovieRetrofit(
        httpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
}
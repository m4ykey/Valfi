package com.example.vuey.core.di

import com.example.vuey.core.common.Constants
import com.example.vuey.feature_album.data.remote.token.LastFmInterceptor
import com.example.vuey.feature_album.data.remote.token.SpotifyInterceptor
import com.example.vuey.feature_artist.data.remote.api.ArtistLastFmApi
import com.example.vuey.feature_movie.data.remote.token.TmdbInterceptor
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
    fun provideLastFmInterceptor(): LastFmInterceptor = LastFmInterceptor()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        spotifyInterceptor: SpotifyInterceptor,
        tmdbInterceptor: TmdbInterceptor,
        lastFmInterceptor: LastFmInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(tmdbInterceptor)
        .addInterceptor(spotifyInterceptor)
        .addInterceptor(lastFmInterceptor)
        .readTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(2, TimeUnit.MINUTES)
        .build()


    @Provides
    @Singleton
    @Named("Auth")
    fun provideSpotifyAuth(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(Constants.SPOTIFY_AUTH_URL)
            .build()

    @Provides
    @Singleton
    @Named("Album")
    fun provideAlbumRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .baseUrl(Constants.SPOTIFY_BASE_URL)
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
            .baseUrl(Constants.TMDB_BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    @Named("Artist")
    fun provideArtistInfoApi(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .baseUrl(Constants.SPOTIFY_BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideArtistBioApi(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): ArtistLastFmApi {
        return Retrofit.Builder()
            .baseUrl(Constants.LAST_FM_BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ArtistLastFmApi::class.java)
    }

}
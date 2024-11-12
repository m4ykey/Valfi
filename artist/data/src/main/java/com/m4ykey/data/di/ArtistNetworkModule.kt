package com.m4ykey.data.di

import com.m4ykey.authentication.interceptor.token.CustomTokenProvider
import com.m4ykey.core.Constants
import com.m4ykey.core.network.createApi
import com.m4ykey.data.remote.api.ArtistApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ArtistNetworkModule {

    @Provides
    @Singleton
    fun provideArtistApi(moshi : Moshi) : ArtistApi = createApi(Constants.SPOTIFY_BASE_URL, moshi)

    @Provides
    @Singleton
    @Named("artist")
    fun provideSpotifyInterceptor(
        interceptor: CustomTokenProvider,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            interceptor.intercept(chain)
        }
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

}
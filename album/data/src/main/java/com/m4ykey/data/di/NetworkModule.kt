package com.m4ykey.data.di

import com.m4ykey.core.Constants
import com.m4ykey.core.network.createApi
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.api.AuthApi
import com.m4ykey.data.remote.interceptor.token.CustomTokenProvider
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
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthApi(moshi: Moshi): AuthApi = createApi(Constants.SPOTIFY_AUTH_URL, moshi)

    @Provides
    @Singleton
    fun provideAlbumApi(moshi: Moshi): AlbumApi = createApi(Constants.SPOTIFY_BASE_URL, moshi)

    @Provides
    @Singleton
    @Named("album")
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
package com.m4ykey.data.di

import android.util.Log
import com.m4ykey.authentication.interceptor.token.CustomTokenProvider
import com.m4ykey.core.Constants
import com.m4ykey.core.network.createApi
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.api.TrackApi
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
object AlbumNetworkModule {

    @Provides
    @Singleton
    fun provideAlbumApi(moshi: Moshi): AlbumApi = createApi(Constants.SPOTIFY_BASE_URL, moshi)

    @Provides
    @Singleton
    fun provideTrackApi(moshi: Moshi): TrackApi = createApi(Constants.SPOTIFY_BASE_URL, moshi)

    @Provides
    @Singleton
    @Named("album")
    fun provideSpotifyInterceptor(
        interceptor: CustomTokenProvider
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.i("AlbumInterceptor", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                interceptor.intercept(chain)
            }
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

}
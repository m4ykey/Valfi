package com.lyrics.data.di

import com.lyrics.data.remote.api.LyricsApi
import com.lyrics.data.remote.api.TrackApi
import com.m4ykey.core.Constants
import com.m4ykey.core.network.createApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LyricsNetworkModule {

    @Provides
    @Singleton
    fun provideLyricsApi(moshi: Moshi) : LyricsApi = createApi(Constants.LYRICS_BASE_URL, moshi)

    @Provides
    @Singleton
    fun provideTrackApi(moshi: Moshi) : TrackApi = createApi(Constants.SPOTIFY_BASE_URL, moshi)

}
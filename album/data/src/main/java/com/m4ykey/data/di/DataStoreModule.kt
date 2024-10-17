package com.m4ykey.data.di

import com.m4ykey.data.preferences.AlbumPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideAlbumPreferences() : AlbumPreferences = AlbumPreferences()

}
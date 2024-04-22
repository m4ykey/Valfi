package com.m4ykey.core.di

import com.m4ykey.core.AlbumSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Singleton
    @Provides
    fun provideAlbumSettings() : AlbumSettings = AlbumSettings()

}
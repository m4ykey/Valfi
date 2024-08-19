package com.m4ykey.data.di

import com.m4ykey.data.preferences.NewsPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsPreferencesModule {

    @Provides
    @Singleton
    fun provideNewsPreferences() : NewsPreferences = NewsPreferences()
}
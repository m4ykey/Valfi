package com.m4ykey.settings.di

import com.m4ykey.settings.theme.ThemePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ThemePreferencesModule {

    @Provides
    @Singleton
    fun provideThemePreferences() : ThemePreferences  = ThemePreferences()

}
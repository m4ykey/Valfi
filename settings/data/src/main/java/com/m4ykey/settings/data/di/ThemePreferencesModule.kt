package com.m4ykey.settings.data.di

import android.content.Context
import com.m4ykey.settings.data.theme.ThemePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ThemePreferencesModule {

    @Provides
    @Singleton
    fun provideThemePreferences(@ApplicationContext context : Context) : ThemePreferences = ThemePreferences(context)

}
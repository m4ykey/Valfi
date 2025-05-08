package com.m4ykey.settings.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
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
    fun provideThemeDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.filesDir.resolve("theme_preferences.preferences_pb")
            }
        )
    }

    @Provides
    @Singleton
    fun provideThemePreferences(dataStore: DataStore<Preferences>) : ThemePreferences {
        return ThemePreferences(dataStore)
    }

}
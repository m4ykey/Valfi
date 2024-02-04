package com.m4ykey.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "spotify_token")

    @Provides
    @Singleton
    fun provideSpotifyDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

}
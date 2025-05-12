package com.m4ykey.data.di

import android.content.Context
import com.m4ykey.core.di.Prefs
import com.m4ykey.data.preferences.AlbumPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlbumDataStoreModule {

    @Provides
    @Singleton
    @Prefs.AlbumPrefs
    fun provideAlbumPreferences(@ApplicationContext context : Context) : AlbumPreferences {
        return AlbumPreferences(context)
    }

}
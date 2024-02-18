package com.m4ykey.core.di

import android.content.Context
import com.m4ykey.core.sort.PreferencesManager
import com.m4ykey.core.sort.PreferencesManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesManagerModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context : Context) : PreferencesManager = PreferencesManagerImpl(context)

}
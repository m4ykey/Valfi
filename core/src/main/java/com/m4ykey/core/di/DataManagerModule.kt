package com.m4ykey.core.di

import com.m4ykey.core.DataManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataManagerModule {

    @Singleton
    @Provides
    fun provideDataManager() : DataManager = DataManager()

}
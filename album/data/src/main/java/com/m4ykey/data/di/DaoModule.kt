package com.m4ykey.data.di

import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.dao.TrackDao
import com.m4ykey.data.local.database.AlbumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideAlbumDao(database : AlbumDatabase) : AlbumDao = database.albumDao()

    @Provides
    @Singleton
    fun provideTrackDao(database: AlbumDatabase) : TrackDao = database.trackDao()

}
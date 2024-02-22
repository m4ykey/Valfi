package com.m4ykey.data.di

import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.dao.ListenLaterDao
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
    fun provideAlbumDao(db : AlbumDatabase) : AlbumDao = db.albumDao()

    @Provides
    @Singleton
    fun provideListenLaterDao(db : AlbumDatabase) : ListenLaterDao = db.listenLaterDao()

}
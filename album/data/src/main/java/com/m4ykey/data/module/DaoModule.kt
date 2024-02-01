package com.m4ykey.data.module

import com.m4ykey.data.local.dao.AlbumDao
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
}
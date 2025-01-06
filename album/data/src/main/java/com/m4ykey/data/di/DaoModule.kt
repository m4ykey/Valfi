package com.m4ykey.data.di

import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.dao.SearchResultDao
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.local.database.SearchResultDatabase
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
    fun provideSearchResultDao(database: SearchResultDatabase) : SearchResultDao = database.searchResultDao()

}
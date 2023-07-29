package com.example.vuey.core.di

import com.example.vuey.feature_album.data.local.source.AlbumLocalDataSource
import com.example.vuey.feature_album.data.local.source.AlbumLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideAlbumDataSource(dataSource : AlbumLocalDataSourceImpl) : AlbumLocalDataSource {
        return dataSource
    }

}
package com.example.vuey.core.di

import com.example.vuey.feature_album.data.local.source.AlbumLocalDataSource
import com.example.vuey.feature_album.data.local.source.AlbumLocalDataSourceImpl
import com.example.vuey.feature_movie.data.local.source.MovieLocalDataSource
import com.example.vuey.feature_movie.data.local.source.MovieLocalDataSourceImpl
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

    @Provides
    fun provideMovieDataSource(dataSource : MovieLocalDataSourceImpl) : MovieLocalDataSource {
        return dataSource
    }

}
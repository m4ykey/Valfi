package com.example.vuey.core.di

import com.example.vuey.feature_album.data.repository.AlbumRepositoryImpl
import com.example.vuey.feature_album.domain.repository.AlbumRepository
import com.example.vuey.feature_movie.data.repository.MovieRepositoryImpl
import com.example.vuey.feature_movie.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAlbumRepository(albumRepository: AlbumRepositoryImpl) : AlbumRepository {
        return albumRepository
    }

    @Provides
    fun provideMovieRepository(movieRepository : MovieRepositoryImpl) : MovieRepository {
        return movieRepository
    }

}
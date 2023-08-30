package com.m4ykey.repository.di

import com.m4ykey.repository.album.AlbumLocalRepository
import com.m4ykey.repository.album.AlbumLocalRepositoryImpl
import com.m4ykey.repository.album.AlbumRepository
import com.m4ykey.repository.album.AlbumRepositoryImpl
import com.m4ykey.repository.movie.MovieLocalRepository
import com.m4ykey.repository.movie.MovieLocalRepositoryImpl
import com.m4ykey.repository.movie.MovieRepository
import com.m4ykey.repository.movie.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAlbumRepository(albumRepository: AlbumRepositoryImpl) : AlbumRepository { return albumRepository }

    @Provides
    @Singleton
    fun provideMovieRepository(movieRepository : MovieRepositoryImpl) : MovieRepository { return movieRepository }

    @Provides
    @Singleton
    fun provideAlbumLocalRepository(albumRepository: AlbumLocalRepositoryImpl) : AlbumLocalRepository { return albumRepository }

    @Provides
    @Singleton
    fun provideMovieLocalRepository(movieRepository : MovieLocalRepositoryImpl) : MovieLocalRepository { return movieRepository }

}
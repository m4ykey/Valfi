package com.m4ykey.data.di

import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.LyricsRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.repository.AlbumRepositoryImpl
import com.m4ykey.data.repository.LyricsRepositoryImpl
import com.m4ykey.data.repository.TrackRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AlbumRepositoryModule {

    @Provides
    @Singleton
    fun provideAlbumRepository(repository: AlbumRepositoryImpl) : AlbumRepository = repository

    @Provides
    @Singleton
    fun provideTrackRepository(repository : TrackRepositoryImpl) : TrackRepository = repository

    @Provides
    @Singleton
    fun provideLyricsRepository(repository : LyricsRepositoryImpl) : LyricsRepository = repository

}
package com.m4ykey.data.di

import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.ArtistRepository
import com.m4ykey.data.domain.repository.SearchResultRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.repository.AlbumRepositoryImpl
import com.m4ykey.data.repository.ArtistRepositoryImpl
import com.m4ykey.data.repository.SearchResultRepositoryImpl
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
    fun provideSearchResultRepository(repository : SearchResultRepositoryImpl) : SearchResultRepository = repository

    @Provides
    @Singleton
    fun provideArtistRepository(repository : ArtistRepositoryImpl) : ArtistRepository = repository

}
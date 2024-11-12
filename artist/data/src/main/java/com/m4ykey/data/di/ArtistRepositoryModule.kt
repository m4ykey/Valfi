package com.m4ykey.data.di

import com.m4ykey.data.domain.repository.ArtistRepository
import com.m4ykey.data.repository.ArtistRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArtistRepositoryModule {

    @Provides
    @Singleton
    fun provideArtistRepository(repository : ArtistRepositoryImpl) : ArtistRepository = repository

}
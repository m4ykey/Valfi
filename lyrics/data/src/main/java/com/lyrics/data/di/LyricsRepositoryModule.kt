package com.lyrics.data.di

import com.lyrics.data.domain.repository.LyricsRepository
import com.lyrics.data.repository.LyricsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LyricsRepositoryModule {

    @Provides
    @Singleton
    fun provideLyricsRepository(repository: LyricsRepositoryImpl) : LyricsRepository = repository

}
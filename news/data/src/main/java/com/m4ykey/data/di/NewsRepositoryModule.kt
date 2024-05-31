package com.m4ykey.data.di

import com.m4ykey.data.domain.repository.NewsRepository
import com.m4ykey.data.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsRepositoryModule {

    @Provides
    @Singleton
    fun provideNewsRepository(repository : NewsRepositoryImpl) : NewsRepository = repository

}
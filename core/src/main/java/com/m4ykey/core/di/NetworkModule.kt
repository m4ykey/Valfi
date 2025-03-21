package com.m4ykey.core.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshiAdapter(): Moshi = Moshi.Builder()
        .addLast(LenientAdapterFactory())
        .addLast(KotlinJsonAdapterFactory())
        .build()
}
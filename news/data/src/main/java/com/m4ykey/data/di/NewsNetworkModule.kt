package com.m4ykey.data.di

import com.m4ykey.core.Constants
import com.m4ykey.core.network.createApi
import com.m4ykey.data.remote.api.NewsApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsNetworkModule {

    @Provides
    @Singleton
    fun provideNewsApi(moshi: Moshi) : NewsApi = createApi(Constants.NEWS_BASE_URL, moshi)
}
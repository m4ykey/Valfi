package com.m4ykey.authentication.di

import com.m4ykey.authentication.api.AuthApi
import com.m4ykey.core.Constants
import com.m4ykey.core.network.createApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(moshi: Moshi): AuthApi = createApi(Constants.SPOTIFY_AUTH_URL, moshi)

}
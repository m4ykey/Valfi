package com.m4ykey.ui.navigation

import androidx.navigation.NavController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlbumNavigatorModule {

    @Provides
    @Singleton
    fun provideAlbumNavigator(navController : NavController) : AlbumNavigator {
        return AlbumNavigatorImpl(navController)
    }

}
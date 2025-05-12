package com.m4ykey.core.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object Prefs {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AlbumPrefs

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ThemePrefs

}
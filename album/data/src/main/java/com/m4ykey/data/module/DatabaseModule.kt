package com.m4ykey.data.module

import android.app.Application
import androidx.room.Room
import com.m4ykey.data.local.database.AlbumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAlbumDatabase(application: Application) : AlbumDatabase {
        return Room.databaseBuilder(
            application,
            AlbumDatabase::class.java,
            "album_db"
        ).fallbackToDestructiveMigration().build()
    }

}
package com.m4ykey.data.di

import android.content.Context
import androidx.room.Room
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.local.database.migration.MIGRATION_1_TO_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAlbumDatabase(@ApplicationContext context: Context) : AlbumDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AlbumDatabase::class.java,
            "album_database"
        ).addMigrations(MIGRATION_1_TO_2).build()
    }

}
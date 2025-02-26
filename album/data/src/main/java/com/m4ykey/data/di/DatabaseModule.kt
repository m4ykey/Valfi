package com.m4ykey.data.di

import android.content.Context
import androidx.room.Room
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.local.database.SearchResultDatabase
import com.m4ykey.data.local.database.migration.AlbumEntityMigrations
import com.m4ykey.data.local.database.migration.SearchResultMigrations
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
    fun provideAlbumDatabase(
        @ApplicationContext context: Context
    ) : AlbumDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AlbumDatabase::class.java,
            "album_database"
        ).addMigrations(AlbumEntityMigrations.MIGRATION_2_3).build()
    }

    @Provides
    @Singleton
    fun provideSearchResultDatabase(
        @ApplicationContext context: Context
    ) : SearchResultDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SearchResultDatabase::class.java,
            "search_result_database"
        ).addMigrations(SearchResultMigrations.MIGRATION_1_2).build()
    }

}
package com.m4ykey.authentication.di

import android.content.Context
import androidx.room.Room
import com.m4ykey.authentication.data.dao.KeyDao
import com.m4ykey.authentication.data.database.KeyDatabase
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
    fun provideKeyDatabase(
        @ApplicationContext context : Context
    ) : KeyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            KeyDatabase::class.java,
            "key_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideKeyDao(database : KeyDatabase) : KeyDao = database.keyDao()

}
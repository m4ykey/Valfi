package com.m4ykey.local.di

import android.app.Application
import androidx.room.Room
import com.m4ykey.local.album.dao.AlbumDao
import com.m4ykey.local.database.VueyDatabase
import com.m4ykey.local.movie.dao.MovieDao
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
    fun provideAppDatabase(application: Application): VueyDatabase {
        return Room.databaseBuilder(
            application,
            VueyDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlbumDao(appDatabase: VueyDatabase): AlbumDao {
        return appDatabase.albumDao()
    }

    @Provides
    @Singleton
    fun provideMovieDao(appDatabase: VueyDatabase): MovieDao {
        return appDatabase.movieDao()
    }

}
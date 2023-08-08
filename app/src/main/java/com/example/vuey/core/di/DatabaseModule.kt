package com.example.vuey.core.di

import android.app.Application
import androidx.room.Room
import com.example.vuey.core.common.Constants.DATABASE_NAME
import com.example.vuey.core.common.database.VueyDatabase
import com.example.vuey.feature_album.data.local.source.dao.AlbumDao
import com.example.vuey.feature_movie.data.local.source.dao.MovieDao
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
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
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
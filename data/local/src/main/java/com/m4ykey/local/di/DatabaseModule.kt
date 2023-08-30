package com.m4ykey.local.di

import android.app.Application
import androidx.room.Room
import com.m4ykey.local.album.converter.AlbumConverter
import com.m4ykey.local.album.dao.AlbumDao
import com.m4ykey.local.database.VueyDatabase
import com.m4ykey.local.movie.converter.MovieConverter
import com.m4ykey.local.movie.dao.MovieDao
import com.squareup.moshi.Moshi
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
    fun provideAppDatabase(application: Application, moshi: Moshi): VueyDatabase {
        val albumConverter = AlbumConverter(moshi)
        val movieConverter = MovieConverter(moshi)
        return Room.databaseBuilder(
            application,
            VueyDatabase::class.java,
            "app_database"
        )
            .addTypeConverter(albumConverter)
            .addTypeConverter(movieConverter)
            .build()
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
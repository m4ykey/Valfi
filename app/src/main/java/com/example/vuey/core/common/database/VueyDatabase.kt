package com.example.vuey.core.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vuey.feature_album.data.local.converter.AlbumConverter
import com.example.vuey.feature_album.data.local.dao.AlbumDao
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_movie.data.local.converter.MovieConverter
import com.example.vuey.feature_movie.data.local.dao.MovieDao
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.core.common.Constants.DATABASE_VERSION
import com.example.vuey.feature_album.data.local.entity.ListenLaterEntity
import com.example.vuey.feature_movie.data.local.entity.WatchLaterEntity

@Database(
    entities = [
        AlbumEntity::class,
        MovieEntity::class,
        ListenLaterEntity::class,
        WatchLaterEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(AlbumConverter::class, MovieConverter::class)
abstract class VueyDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun movieDao(): MovieDao

}
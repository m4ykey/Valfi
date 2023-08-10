package com.example.vuey.core.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vuey.feature_album.data.local.source.converter.AlbumConverter
import com.example.vuey.feature_album.data.local.source.dao.AlbumDao
import com.example.vuey.feature_album.data.local.source.entity.AlbumEntity
import com.example.vuey.feature_movie.data.local.source.converter.MovieConverter
import com.example.vuey.feature_movie.data.local.source.dao.MovieDao
import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import com.example.vuey.core.common.Constants.DATABASE_VERSION
import com.example.vuey.feature_album.data.local.source.entity.ListenLaterEntity
import com.example.vuey.feature_movie.data.local.source.entity.WatchLaterEntity

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
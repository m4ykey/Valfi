package com.m4ykey.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.m4ykey.local.album.converter.AlbumConverter
import com.m4ykey.local.album.dao.AlbumDao
import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
import com.m4ykey.local.movie.converter.MovieConverter
import com.m4ykey.local.movie.dao.MovieDao
import com.m4ykey.local.movie.entity.MovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity

@Database(
    entities = [
        AlbumEntity::class,
        MovieEntity::class,
        ListenLaterEntity::class,
        WatchLaterEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(AlbumConverter::class, MovieConverter::class)
abstract class VueyDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun movieDao(): MovieDao

}
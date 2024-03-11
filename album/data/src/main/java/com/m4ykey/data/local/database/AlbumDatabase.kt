package com.m4ykey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.dao.ListenLaterDao
import com.m4ykey.data.local.dao.TrackDao
import com.m4ykey.data.local.database.converter.TrackAlbumConverter
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ListenLaterEntity
import com.m4ykey.data.local.model.TrackEntity

@Database(
    entities = [AlbumEntity::class, ListenLaterEntity::class, TrackEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(TrackAlbumConverter::class)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao
    abstract fun listenLaterDao() : ListenLaterDao
    abstract fun trackDao() : TrackDao

}
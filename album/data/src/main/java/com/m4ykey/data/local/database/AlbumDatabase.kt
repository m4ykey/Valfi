package com.m4ykey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.dao.TrackDao
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.TrackEntity

@Database(
    version = 3,
    exportSchema = false,
    entities = [
        AlbumEntity::class,
        IsAlbumSaved::class,
        IsListenLaterSaved::class,
        TrackEntity::class
    ]
)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao
    abstract fun trackDao() : TrackDao

}
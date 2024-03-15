package com.m4ykey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        AlbumEntity::class,
        IsAlbumSaved::class,
        IsListenLaterSaved::class
    ]
)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao

}
package com.m4ykey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.model.AlbumEntity

@Database(
    entities = [AlbumEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao

}
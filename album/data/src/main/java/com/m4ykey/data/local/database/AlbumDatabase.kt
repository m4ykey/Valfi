package com.m4ykey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ListenLaterEntity

@Database(
    entities = [AlbumEntity::class, ListenLaterEntity::class],
    version = 6,
    exportSchema = false
)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao

}
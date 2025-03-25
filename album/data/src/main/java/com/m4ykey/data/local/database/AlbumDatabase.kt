package com.m4ykey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.dao.TrackDao
import com.m4ykey.data.local.database.converter.Converters
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ArtistEntity
import com.m4ykey.data.local.model.CopyrightEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.StarsEntity
import com.m4ykey.data.local.model.TrackEntity

@Database(
    version = 5,
    exportSchema = false,
    entities = [
        AlbumEntity::class,
        IsAlbumSaved::class,
        IsListenLaterSaved::class,
        TrackEntity::class,
        StarsEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao
    abstract fun trackDao() : TrackDao

}
package com.m4ykey.authentication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4ykey.authentication.data.dao.KeyDao
import com.m4ykey.authentication.data.model.KeyEntity

@Database(
    entities = [KeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KeyDatabase : RoomDatabase() {

    abstract fun keyDao() : KeyDao

}
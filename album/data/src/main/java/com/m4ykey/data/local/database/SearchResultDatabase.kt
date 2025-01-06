package com.m4ykey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4ykey.data.local.dao.SearchResultDao
import com.m4ykey.data.local.model.SearchResult

@Database(
    version = 1,
    exportSchema = false,
    entities = [SearchResult::class]
)
abstract class SearchResultDatabase : RoomDatabase() {

    abstract fun searchResultDao() : SearchResultDao

}
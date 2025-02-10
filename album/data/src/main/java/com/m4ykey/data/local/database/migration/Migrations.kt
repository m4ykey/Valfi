package com.m4ykey.data.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DELETE FROM search_result_table WHERE id NOT IN (SELECT MIN(id) FROM search_result_table GROUP BY name)")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_search_result_table_name ON search_result_table(name)")
    }
}
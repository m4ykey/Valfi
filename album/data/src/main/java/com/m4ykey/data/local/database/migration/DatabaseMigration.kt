package com.m4ykey.data.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_TO_2 : Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `track_table` {" +
            "`id` TEXT PRIMARY KEY," +
            "`albumId` TEXT," +
            "`name` TEXT," +
            "`externalUrls` TEXT," +
            "`explicit` INTEGER," +
            "`durationMs` INTEGER," +
            "`artistList` TEXT" +
            "}"
        )
    }
}
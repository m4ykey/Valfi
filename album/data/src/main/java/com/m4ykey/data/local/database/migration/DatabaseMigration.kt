package com.m4ykey.data.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_TO_2 : Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE album_table ADD COLUMN saveTime INTEGER NOT NULL DEFAULT 0")
    }
}
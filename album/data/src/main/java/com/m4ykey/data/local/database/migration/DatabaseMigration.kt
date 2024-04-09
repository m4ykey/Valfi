package com.m4ykey.data.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_TO_2 : Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE album_table ADD COLUMN saveTime INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_2_TO_3 : Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // album_table
        db.execSQL("CREATE INDEX IF NOT EXISTS index_album_type ON album_table (albumType)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_album_name ON album_table (name)")

        // album_saved_table
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_album_saved_album_id ON album_saved_table (albumId)")

        // listen_later_table
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_listen_later_album_id ON listen_later_table (albumId)")

        // track_table
        db.execSQL("CREATE INDEX IF NOT EXISTS index_track_album_id ON track_table (albumId)")
    }
}
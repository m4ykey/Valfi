package com.m4ykey.data.local.database.migration

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.m4ykey.data.local.database.converter.Converters
import com.m4ykey.data.local.model.ArtistEntity

class DatabaseMigration {

    private val MIGRATION_1_TO_2 : Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE album_table ADD COLUMN saveTime INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_2_TO_3 : Migration = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE INDEX IF NOT EXISTS index_album_type ON album_table (albumType)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_album_name ON album_table (name)")
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_album_saved_album_id ON album_saved_table (albumId)")
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_listen_later_album_id ON listen_later_table (albumId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_track_album_id ON track_table (albumId)")
        }
    }

    private val MIGRATION_3_TO_4 : Migration = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE track_table ADD COLUMN discNumber INTEGER NOT NULL DEFAULT 0")
            db.execSQL("""
                   CREATE TABLE IF NOT EXISTS artist_table (
                        albumId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        urls TEXT NOT NULL,
                        artistId TEXT NOT NULL,
                        PRIMARY KEY (albumId, artistId)
                   ) 
            """)

            db.query("SELECT id, artists FROM album_table").use { cursor ->
                val albumIdIndex = cursor.getColumnIndex("id")
                val artistsIndex = cursor.getColumnIndex("artists")

                while (cursor.moveToNext()) {
                    val albumId = cursor.getString(albumIdIndex)
                    val artistJson = cursor.getString(artistsIndex)

                    val artistsEntities : List<ArtistEntity> = Converters().adapter.fromJson(artistJson) ?: emptyList()

                    for (artist in artistsEntities) {
                        val contentValues = ContentValues().apply {
                            put("albumId", albumId)
                            put("name", artist.name)
                            put("urls", artist.urls)
                            put("artistId", artist.artistId)
                        }
                        db.insert("artist_table", SQLiteDatabase.CONFLICT_REPLACE, contentValues)
                    }
                }
            }

            db.execSQL("ALTER TABLE album_table DROP COLUMN artists")
        }
    }

    fun getAllMigrations() = listOf(
        MIGRATION_1_TO_2,
        MIGRATION_2_TO_3,
        MIGRATION_3_TO_4
    )
}
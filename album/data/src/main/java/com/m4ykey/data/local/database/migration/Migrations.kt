package com.m4ykey.data.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object SearchResultMigrations {
    val MIGRATION_1_2 = object : Migration(1,2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DELETE FROM search_result_table WHERE id NOT IN (SELECT MIN(id) FROM search_result_table GROUP BY name)")
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_search_result_table_name ON search_result_table(name)")
        }
    }
}

object AlbumEntityMigrations {
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                    CREATE TABLE album_table_new (
                    album_type TEXT NOT NULL,
                    artists TEXT NOT NULL,
                    album_url TEXT NOT NULL,
                    id TEXT NOT NULL PRIMARY KEY,
                    images TEXT NOT NULL,
                    name TEXT NOT NULL,
                    release_date TEXT NOT NULL,
                    total_tracks INTEGER NOT NULL,
                    save_time INTEGER
                    )
                """.trimIndent()
            )

            db.execSQL(
                """
                    INSERT INTO album_table_new (album_type, artists, album_url, id, images, name, release_date, total_tracks, save_time)
                    SELECT album_type, artists, album_url, id, images, name, release_date, total_tracks, save_time FROM album_table
                """.trimIndent()
            )

            db.execSQL("DROP TABLE album_table")

            db.execSQL("ALTER TABLE album_table_new RENAME TO album_table")

            db.execSQL(
                """
                    CREATE UNIQUE INDEX index_album_table_album_type_name
                    ON album_table (album_type, name)
                """.trimIndent()
            )
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `track_table` (
                    `id` TEXT NOT NULL,
                    `durationMs` INTEGER NOT NULL,
                    `albumId` TEXT NOT NULL,
                    `name` TEXT NOT NULL,
                    `explicit` INTEGER NOT NULL,
                    `artists` TEXT NOT NULL,
                    PRIMARY KEY(`id`)
                )
            """.trimIndent())

            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `album_table_new` (
                    `album_type` TEXT NOT NULL,
                    `artists` TEXT NOT NULL,
                    `album_url` TEXT NOT NULL,
                    `id` TEXT NOT NULL PRIMARY KEY,
                    `images` TEXT NOT NULL,
                    `name` TEXT NOT NULL,
                    `release_date` TEXT NOT NULL,
                    `total_tracks` INTEGER NOT NULL,
                    `save_time` INTEGER,
                    `copyrights` TEXT NOT NULL DEFAULT '[]'
                )
            """.trimIndent())

            db.execSQL("""
                INSERT INTO `album_table_new` (
                    `album_type`, `artists`, `album_url`, `id`, `images`, 
                    `name`, `release_date`, `total_tracks`, `save_time`,
                    `copyrights`
                )
                SELECT
                    `album_type`, `artists`, `album_url`, `id`, `images`, 
                    `name`, `release_date`, `total_tracks`, `save_time`,
                    '[]'
                FROM `album_table`
            """.trimIndent())

            db.execSQL("DROP TABLE `album_table`")

            db.execSQL("ALTER TABLE `album_table_new` RENAME TO `album_table`")

            db.execSQL("""
                CREATE UNIQUE INDEX IF NOT EXISTS `index_album_table_album_type_name`
                ON `album_table` (`album_type`, `name`)
            """.trimIndent())

            db.execSQL("""
            CREATE TABLE IF NOT EXISTS `copyright_table` (
                `text` TEXT NOT NULL PRIMARY KEY
            )
        """.trimIndent())
        }
    }

    val albumMigrations = listOf(
        MIGRATION_2_3,
        MIGRATION_3_4
    )
}
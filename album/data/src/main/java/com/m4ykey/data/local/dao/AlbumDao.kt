package com.m4ykey.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.AlbumWithDetails
import com.m4ykey.data.local.model.DecadeResult
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album : AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved)

    @Query("SELECT * FROM album_table WHERE id = :albumId")
    suspend fun getAlbum(albumId : String) : AlbumEntity?

    @Query("SELECT * FROM album_saved_table WHERE albumId = :albumId")
    suspend fun getSavedAlbumState(albumId : String) : IsAlbumSaved?

    @Query("SELECT * FROM listen_later_table WHERE albumId = :albumId")
    suspend fun getListenLaterState(albumId : String) : IsListenLaterSaved?

    @Transaction
    @Query("SELECT * FROM album_table WHERE id = :albumId")
    suspend fun getAlbumWithStates(albumId: String) : AlbumWithStates?

    @Query("DELETE FROM album_table WHERE id = :albumId")
    suspend fun deleteAlbum(albumId: String)

    @Query("DELETE FROM album_saved_table WHERE albumId = :albumId")
    suspend fun deleteSavedAlbumState(albumId: String)

    @Query("DELETE FROM listen_later_table WHERE albumId = :albumId")
    suspend fun deleteListenLaterState(albumId: String)

    @Query("SELECT * FROM album_table INNER JOIN album_saved_table ON " +
            "album_table.id = album_saved_table.albumId WHERE " +
            "album_saved_table.isAlbumSaved = 1 ORDER BY save_time DESC")
    suspend fun getSavedAlbums() : List<AlbumEntity>

    @Query("SELECT * FROM album_table INNER JOIN listen_later_table ON " +
            "album_table.id = listen_later_table.albumId WHERE " +
            "listen_later_table.isListenLaterSaved = 1 ORDER BY name ASC")
    suspend fun getListenLaterAlbums() : List<AlbumEntity>

    @Query("SELECT * FROM album_table INNER JOIN listen_later_table ON " +
            "album_table.id = listen_later_table.albumId WHERE " +
            "listen_later_table.isListenLaterSaved = 1 ORDER BY RANDOM() LIMIT 1")
    fun getRandomAlbum() : Flow<AlbumEntity?>

    @Query("SELECT COUNT(*) FROM listen_later_table WHERE isListenLaterSaved = 1")
    fun getListenLaterCount() : Flow<Int>

    @Query("SELECT * FROM album_table INNER JOIN album_saved_table ON " +
            "album_table.id = album_saved_table.albumId WHERE " +
            "album_saved_table.isAlbumSaved = 1 AND album_table.album_type = :albumType")
    suspend fun getAlbumType(albumType : String) : List<AlbumEntity>

    @Query("SELECT * FROM album_table INNER JOIN album_saved_table ON " +
            "album_table.id = album_saved_table.albumId WHERE " +
            "album_saved_table.isAlbumSaved = 1 AND album_table.name " +
            "LIKE '%' || :searchQuery || '%'")
    suspend fun searchAlbumsByName(searchQuery : String) : List<AlbumEntity>

    @Query("SELECT * FROM album_table INNER JOIN listen_later_table ON " +
            "album_table.id = listen_later_table.albumId WHERE " +
            "listen_later_table.isListenLaterSaved = 1 AND album_table.name " +
            "LIKE '%' || :searchQuery || '%'")
    suspend fun searchAlbumsListenLater(searchQuery: String) : List<AlbumEntity>

    @Query("SELECT * FROM album_table INNER JOIN album_saved_table ON " +
            "album_table.id = album_saved_table.albumId WHERE " +
            "album_saved_table.isAlbumSaved = 1 ORDER BY name")
    suspend fun getAlbumSortedByName() : List<AlbumEntity>

    @Query("SELECT * FROM album_table INNER JOIN album_saved_table ON " +
            "album_table.id = album_saved_table.albumId WHERE " +
            "album_saved_table.isAlbumSaved = 1 ORDER BY save_time ASC")
    suspend fun getSavedAlbumAsc() : List<AlbumEntity>

    @Query("SELECT COUNT(*) FROM album_table INNER JOIN album_saved_table ON " +
            "album_table.id = album_saved_table.albumId WHERE " +
            "album_saved_table.isAlbumSaved = 1")
    fun getAlbumCount() : Flow<Int>

    @Query("SELECT SUM(album_table.total_tracks) FROM album_table INNER JOIN album_saved_table ON " +
            "album_table.id = album_saved_table.albumId WHERE " +
            "album_saved_table.isAlbumSaved = 1")
    fun getTotalTracksCount() : Flow<Int>

    @Query("""
        SELECT
            (CAST(SUBSTR(release_date, 1, 4) / 10 AS INTEGER) * 10) AS decade,
            COUNT(*) AS album_count
        FROM album_table
        JOIN album_saved_table ON album_table.id = album_saved_table.albumId
        WHERE album_saved_table.isAlbumSaved = 1
        GROUP BY decade
        ORDER BY album_count DESC
        LIMIT 1
    """)
    fun getMostPopularDecade() : Flow<DecadeResult>

    @Query("""
        SELECT COUNT(*) AS album_count
        FROM album_table 
        INNER JOIN album_saved_table 
        ON album_table.id = album_saved_table.albumId 
        WHERE album_saved_table.isAlbumSaved = 1 AND album_table.album_type = :albumType
    """)
    fun getAlbumCountByType(albumType : String) : Flow<Int>

    @Query("""
    SELECT album_table.name AS album_name, 
           album_table.images AS album_image, 
           album_table.total_tracks AS album_total_tracks
    FROM album_table
    INNER JOIN album_saved_table ON album_table.id = album_saved_table.albumId
    WHERE album_saved_table.isAlbumSaved = 1
    ORDER BY album_table.total_tracks DESC
    LIMIT 1
""")
    fun getAlbumWithMostTracks(): Flow<AlbumWithDetails>
}
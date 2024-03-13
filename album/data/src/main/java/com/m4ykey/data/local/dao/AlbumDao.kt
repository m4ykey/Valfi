package com.m4ykey.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m4ykey.data.local.model.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album : AlbumEntity)

    @Delete
    suspend fun deleteAlbum(album: AlbumEntity)

    @Query("UPDATE album SET isAlbumSaved = :isSaved WHERE id = :albumId")
    suspend fun updateAlbumSaved(albumId: String, isSaved : Boolean)

    @Query("UPDATE album SET isListenLaterSaved = :isListenLater WHERE id = :albumId")
    suspend fun updateListenLaterSaved(albumId: String, isListenLater : Boolean)

    @Query("SELECT * FROM album WHERE isAlbumSaved = 1 ORDER BY name COLLATE NOCASE ASC")
    fun getAlbumSortedAlphabetical() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE isAlbumSaved = 1 ORDER BY saveTime DESC")
    fun getAlbumsRecentlyAdded() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE albumType = 'Album' AND isAlbumSaved = 1 ORDER BY name ASC")
    fun getAlbumsOfTypeAlbumPaged() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE albumType = 'EP' AND isAlbumSaved = 1 ORDER BY name ASC")
    fun getAlbumsOfTypeEPPaged() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE albumType = 'Single' AND isAlbumSaved = 1 ORDER BY name ASC")
    fun getAlbumsOfTypeSinglePaged() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE albumType = 'Compilation' AND isAlbumSaved = 1 ORDER BY name ASC")
    fun getAlbumsOfTypeCompilationPaged() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE id = :albumId LIMIT 1")
    fun getLocalAlbumById(albumId : String?) : Flow<AlbumEntity>

    @Query("SELECT COUNT(*) FROM album WHERE isAlbumSaved = 1")
    fun getAlbumCount() : Int

    @Query("SELECT SUM(totalTracks) FROM album WHERE isAlbumSaved = 1")
    fun getTotalTracksCount() : Int

    @Query("SELECT COUNT(*) FROM album WHERE isListenLaterSaved = 1")
    fun getListenLaterCount() : Int

    @Query("SELECT * FROM album WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchAlbumsByName(searchQuery : String) : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE isListenLaterSaved = 1 ORDER BY saveTime")
    fun getListenLaterAlbums() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE isListenLaterSaved = 1 ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomAlbum() : AlbumEntity?

}
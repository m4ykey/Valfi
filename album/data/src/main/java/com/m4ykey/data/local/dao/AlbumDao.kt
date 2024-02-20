package com.m4ykey.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ListenLaterEntity

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album : AlbumEntity)

    @Delete
    suspend fun deleteAlbum(album: AlbumEntity)

    @Query("SELECT * FROM album ORDER BY name COLLATE NOCASE ASC")
    fun getAlbumSortedAlphabetical() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album ORDER BY saveTime DESC")
    fun getAlbumsRecentlyAdded() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE albumType = 'Album' ORDER BY name ASC")
    fun getAlbumsOfTypeAlbumPaged() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE albumType = 'EP' ORDER BY name ASC")
    fun getAlbumsOfTypeEPPaged() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE albumType = 'Single' ORDER BY name ASC")
    fun getAlbumsOfTypeSinglePaged() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE albumType = 'Compilation' ORDER BY name ASC")
    fun getAlbumsOfTypeCompilationPaged() : PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE id = :albumId")
    suspend fun getLocalAlbumById(albumId : String) : AlbumEntity

    @Query("SELECT * FROM listen_later WHERE id = :albumId")
    suspend fun getListenLaterAlbumById(albumId : String) : ListenLaterEntity

    @Query("SELECT COUNT(*) FROM album")
    fun getAlbumCount() : Int

    @Query("SELECT SUM(totalTracks) FROM album")
    fun getTotalTracksCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListenLater(album : ListenLaterEntity)

    @Delete
    suspend fun deleteListenLater(album: ListenLaterEntity)

    @Query("SELECT COUNT(*) FROM listen_later")
    fun getListenLaterCount() : Int

    @Query("SELECT * FROM listen_later ORDER BY saveTime DESC")
    fun getListenLaterAlbums() : PagingSource<Int, ListenLaterEntity>

}
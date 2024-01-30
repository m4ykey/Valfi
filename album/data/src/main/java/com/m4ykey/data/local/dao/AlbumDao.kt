package com.m4ykey.data.local.dao

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

    @Query("SELECT * FROM album WHERE id = :id")
    suspend fun getAlbumById(id : String) : AlbumEntity

    @Query("SELECT * FROM album ORDER BY name ASC")
    fun getAlbumSortedAlphabetical() : Flow<List<AlbumEntity>>

    @Query("SELECT * FROM album ORDER BY saveTime ASC")
    fun getAllAlbums() : Flow<List<AlbumEntity>>

}
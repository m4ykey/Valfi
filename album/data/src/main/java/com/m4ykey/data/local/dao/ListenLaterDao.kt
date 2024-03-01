package com.m4ykey.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m4ykey.data.local.model.ListenLaterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListenLaterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListenLater(album : ListenLaterEntity)

    @Delete
    suspend fun deleteListenLater(album: ListenLaterEntity)

    @Query("SELECT COUNT(*) FROM listen_later")
    fun getListenLaterCount() : Flow<Int>

    @Query("SELECT * FROM listen_later ORDER BY saveTime DESC")
    fun getListenLaterAlbums() : PagingSource<Int, ListenLaterEntity>

    @Query("SELECT * FROM listen_later WHERE id = :albumId")
    fun getListenLaterAlbumById(albumId : String) : Flow<ListenLaterEntity>

    @Query("SELECT * FROM listen_later ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomAlbum() : ListenLaterEntity?

}
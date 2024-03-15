package com.m4ykey.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.AlbumWithStates
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved

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

}
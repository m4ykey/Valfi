package com.m4ykey.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m4ykey.data.local.model.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track : List<TrackEntity>)

    @Query("SELECT * FROM track_table WHERE albumId = :albumId")
    fun getTracksById(albumId : String) : List<TrackEntity>

    @Query("DELETE FROM track_table WHERE albumId = :albumId")
    suspend fun deleteTracksById(albumId: String)
}
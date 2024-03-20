package com.m4ykey.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m4ykey.data.local.model.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracks(tracks : List<TrackEntity>)

    @Query("SELECT * FROM track_table WHERE albumId = :albumId")
    fun getTrackPagingSource(albumId : String) : PagingSource<Int, TrackEntity>

    @Query("DELETE FROM track_table WHERE albumId = :albumId")
    suspend fun deleteTracks(albumId : String)

}
package com.m4ykey.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.m4ykey.data.local.model.TrackEntity

@Dao
interface TrackDao {

    @Upsert
    suspend fun upsertAll(tracks : List<TrackEntity>)

    @Query("SELECT * FROM album_tracks")
    fun trackPagingSource() : PagingSource<Int, TrackEntity>

    @Query("DELETE FROM album_tracks")
    suspend fun clearAll()

}
package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.TrackEntity
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    suspend fun getAlbumTracks(id : String, offset : Int, limit : Int) : Flow<PagingData<TrackItem>>

    suspend fun insertTracks(track : List<TrackEntity>)
    fun getTracksById(albumId : String) : List<TrackEntity>
    suspend fun deleteTracksById(albumId : String)

}
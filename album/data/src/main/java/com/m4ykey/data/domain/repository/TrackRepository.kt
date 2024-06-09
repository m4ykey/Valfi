package com.m4ykey.data.domain.repository

import com.m4ykey.data.domain.model.track.TrackItem
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    suspend fun getAlbumTracks(id : String, offset : Int, limit : Int) : Flow<List<TrackItem>>

}
package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.track.TrackItem
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun getAlbumTracks(id : String) : Flow<PagingData<TrackItem>>

}
package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.data.local.model.TrackEntity
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun getAlbumTracks(id : String) : Flow<PagingData<TrackEntity>>

}
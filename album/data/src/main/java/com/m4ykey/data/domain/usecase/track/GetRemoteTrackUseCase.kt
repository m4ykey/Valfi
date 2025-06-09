package com.m4ykey.data.domain.usecase.track

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemoteTrackUseCase @Inject constructor(
    private val repository : TrackRepository
) {

    suspend fun getAlbumTracks(offset : Int = 0, limit : Int = 20, id : String) : Flow<PagingData<TrackItem>> {
        return repository.getAlbumTracks(
            offset = offset,
            limit = limit,
            id = id
        )
    }

}
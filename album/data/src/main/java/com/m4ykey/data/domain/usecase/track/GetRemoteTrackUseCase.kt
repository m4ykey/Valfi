package com.m4ykey.data.domain.usecase.track

import com.m4ykey.data.domain.repository.TrackRepository
import javax.inject.Inject

class GetRemoteTrackUseCase @Inject constructor(
    private val repository : TrackRepository
) {

    suspend fun getAlbumTracks(offset : Int = 0, limit : Int = 20, id : String) = repository.getAlbumTracks(
        offset = offset,
        limit = limit,
        id = id
    )

}
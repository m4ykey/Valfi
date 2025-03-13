package com.m4ykey.data.domain.usecase.track

import com.m4ykey.core.UseCase
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.model.TrackEntity
import javax.inject.Inject

class GetLocalTrackUseCase @Inject constructor(
    private val repository: TrackRepository
) : UseCase<GetLocalTrackUseCase.Params, List<TrackEntity>?> {

    sealed class Params {
        data class InsertTracks(val track : List<TrackEntity>) : Params()
        data class GetTrack(val albumId: String) : Params()
        data class DeleteTrack(val albumId : String) : Params()
    }

    override suspend fun invoke(params: Params): List<TrackEntity>? {
        return when (params) {
            is Params.GetTrack -> {
                repository.getTracksById(params.albumId)
            }
            is Params.DeleteTrack -> {
                repository.deleteTracksById(params.albumId)
                null
            }
            is Params.InsertTracks -> {
                repository.insertTracks(params.track)
                null
            }
        }
    }
}
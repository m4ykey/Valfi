package com.m4ykey.data.domain.usecase.stars

import com.m4ykey.core.UseCase
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.model.StarsEntity
import javax.inject.Inject

class GetStarsUseCase @Inject constructor(
    private val repository : AlbumRepository
) : UseCase<GetStarsUseCase.Params, List<StarsEntity>?> {

    sealed class Params {
        data class InsertStars(val stars : List<StarsEntity>) : Params()
        data class GetStars(val albumId : String) : Params()
        data class DeleteStars(val albumId: String) : Params()
    }

    override suspend fun invoke(params: Params): List<StarsEntity>? {
        return when (params) {
            is Params.GetStars -> {
                repository.getStarsById(params.albumId)
            }
            is Params.InsertStars -> {
                repository.insertStars(params.stars)
                null
            }
            is Params.DeleteStars -> {
                repository.deleteStarsById(params.albumId)
                null
            }
        }
    }
}
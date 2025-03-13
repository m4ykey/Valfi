package com.m4ykey.data.domain.usecase.album

import com.m4ykey.core.UseCase
import com.m4ykey.data.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) : UseCase<GetAlbumUseCase.Params, Any?> {

    sealed class Params {
        data class GetAlbum(val id : String) : Params()
        data class GetSavedAlbumState(val id : String) : Params()
        data class GetListenLaterState(val id : String) : Params()
        data class GetAlbumWithStates(val id : String) : Params()
    }

    override suspend fun invoke(params: Params) : Any? {
        return when (params) {
            is Params.GetAlbum -> {
                repository.getAlbum(params.id)
            }
            is Params.GetSavedAlbumState -> {
                repository.getSavedAlbumState(params.id)
            }
            is Params.GetListenLaterState -> {
                repository.getListenLaterState(params.id)
            }
            is Params.GetAlbumWithStates -> {
                repository.getAlbumWithStates(params.id)
            }
        }
    }
}
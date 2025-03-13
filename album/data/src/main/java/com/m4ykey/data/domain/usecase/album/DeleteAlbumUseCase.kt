package com.m4ykey.data.domain.usecase.album

import com.m4ykey.core.UseCase
import com.m4ykey.data.domain.repository.AlbumRepository
import javax.inject.Inject

class DeleteAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) : UseCase<DeleteAlbumUseCase.Params, Unit> {

    sealed class Params {
        data class DeleteAlbum(val albumId : String) : Params()
        data class RemoveFromSaved(val albumId : String) : Params()
        data class RemoveFromListenLater(val albumId: String) : Params()
    }

    override suspend fun invoke(params: Params) {
        when (params) {
            is Params.DeleteAlbum -> {
                repository.deleteAlbum(params.albumId)
            }
            is Params.RemoveFromSaved -> {
                repository.deleteSavedAlbumState(params.albumId)
            }
            is Params.RemoveFromListenLater -> {
                repository.deleteListenLaterState(params.albumId)
            }
        }
    }
}
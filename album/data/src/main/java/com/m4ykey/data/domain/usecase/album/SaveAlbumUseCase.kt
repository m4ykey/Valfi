package com.m4ykey.data.domain.usecase.album

import com.m4ykey.core.UseCase
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import javax.inject.Inject

class SaveAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) : UseCase<SaveAlbumUseCase.Params, Unit> {

    sealed class Params {
        data class SaveAlbum(val album : AlbumEntity) : Params()
        data class MarkAsSaved(val isAlbumSaved : IsAlbumSaved) : Params()
        data class AddToListenLater(val isListenLaterSaved: IsListenLaterSaved) : Params()
    }

    override suspend fun invoke(params: Params) {
        when (params) {
            is Params.SaveAlbum -> {
                repository.insertAlbum(params.album)
            }
            is Params.MarkAsSaved -> {
                repository.insertSavedAlbum(params.isAlbumSaved)
            }
            is Params.AddToListenLater -> {
                repository.insertListenLaterAlbum(params.isListenLaterSaved)
            }
        }
    }
}
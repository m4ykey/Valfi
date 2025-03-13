package com.m4ykey.data.domain.usecase.album

import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.model.AlbumEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    suspend fun getSavedAlbums() : List<AlbumEntity> = repository.getSavedAlbums()

    suspend fun getListenLaterAlbums() : List<AlbumEntity> = repository.getListenLaterAlbums()

    suspend fun getAlbumSortedByName() : List<AlbumEntity> = repository.getAlbumSortedByName()

    suspend fun getSavedAlbumAsc() : List<AlbumEntity> = repository.getSavedAlbumAsc()

    suspend fun searchAlbumsListenLater(query : String) : List<AlbumEntity> = repository.searchAlbumsListenLater(query)

    suspend fun searchAlbumByName(query : String) : List<AlbumEntity> = repository.searchAlbumByName(query)

    suspend fun getAlbumsByType(albumType : String) : List<AlbumEntity> = repository.getAlbumType(albumType)

    fun getRandomAlbum() : Flow<AlbumEntity?> = repository.getRandomAlbum()

}
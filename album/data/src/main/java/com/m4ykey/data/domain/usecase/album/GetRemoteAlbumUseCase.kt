package com.m4ykey.data.domain.usecase.album

import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemoteAlbumUseCase @Inject constructor(
    private val repository : AlbumRepository
)  {

    suspend fun getNewReleases(offset : Int = 0, limit : Int = 20) : Flow<List<AlbumItem>> {
        return repository.getNewReleases(offset, limit)
    }

    suspend fun searchAlbums(query : String, offset: Int = 0, limit : Int = 20) : Flow<List<AlbumItem>> {
        return repository.searchAlbums(query, offset, limit)
    }

    suspend fun getAlbumById(id : String) : Flow<AlbumDetail> {
        return repository.getAlbumById(id)
    }

}
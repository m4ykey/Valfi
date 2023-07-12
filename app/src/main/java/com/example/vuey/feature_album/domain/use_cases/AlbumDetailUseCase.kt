package com.example.vuey.feature_album.domain.use_cases

import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail
import com.example.vuey.feature_album.domain.repository.AlbumRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumDetailUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(albumId : String) : Flow<Resource<AlbumDetail>> {
        return repository.getAlbum(albumId)
    }
}
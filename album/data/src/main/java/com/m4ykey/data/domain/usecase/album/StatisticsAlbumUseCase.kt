package com.m4ykey.data.domain.usecase.album

import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.model.AlbumWithDetails
import com.m4ykey.data.local.model.DecadeResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StatisticsAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    fun getAlbumWithMostTracks() : Flow<AlbumWithDetails> = repository.getAlbumWithMostTracks()

    fun getMostPopularDecade() : Flow<DecadeResult> = repository.getMostPopularDecade()

    fun getListenLaterCount() : Flow<Int> = repository.getListenLaterCount()

    fun getAlbumCount() : Flow<Int> = repository.getAlbumCount()

    fun getTotalTracksCount() : Flow<Int> = repository.getTotalTracksCount()

    fun getAlbumTypeCount(albumType : String) : Flow<Int> = repository.getAlbumCountByType(albumType)

}
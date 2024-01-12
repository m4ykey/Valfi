package com.m4ykey.data.domain.repository

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.AlbumItem
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun searchAlbums(query : String) : Flow<PagingData<AlbumItem>>

}
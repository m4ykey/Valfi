package com.m4ykey.valfi2.album.data.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.valfi2.album.data.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun searchAlbums() : Flow<PagingData<Item>>

}
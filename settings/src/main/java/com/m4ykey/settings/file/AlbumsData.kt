package com.m4ykey.settings.file

import com.m4ykey.data.local.model.AlbumEntity
import kotlinx.coroutines.flow.Flow

data class AlbumsData(
    val savedAlbums : Flow<List<AlbumEntity>>,
    val listenLaterAlbums : Flow<List<AlbumEntity>>
)

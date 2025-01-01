package com.m4ykey.settings.file

import com.m4ykey.data.local.model.AlbumEntity

data class AlbumsData(
    val savedAlbums : List<AlbumEntity>,
    val listenLaterAlbums : List<AlbumEntity>
)

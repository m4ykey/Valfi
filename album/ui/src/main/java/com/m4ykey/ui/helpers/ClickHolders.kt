package com.m4ykey.ui.helpers

import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.SearchResult
import com.m4ykey.ui.colors.ColorList

typealias OnAlbumClick = (AlbumItem) -> Unit
typealias OnAlbumEntityClick = (AlbumEntity) -> Unit
typealias OnTrackClick = (TrackItem) -> Unit
typealias OnColorClick = (ColorList) -> Unit
typealias OnSearchClick = (SearchResult) -> Unit
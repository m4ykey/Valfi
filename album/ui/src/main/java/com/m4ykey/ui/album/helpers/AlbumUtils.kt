package com.m4ykey.ui.album.helpers

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import com.m4ykey.core.views.getArtistsList
import com.m4ykey.core.views.getLargestImageFromUrl
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
import kotlinx.coroutines.launch

fun AlbumItem.getLargestImageUrl() : String? {
    return getLargestImageFromUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}

fun AlbumDetail.getLargestImageUrl() : String? {
    return getLargestImageFromUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}

fun AlbumItem.getArtistList() : String {
    return getArtistsList(
        this,
        { it.artists },
        { it.name }
    )
}

fun AlbumEntity.getArtistList() : String {
    return getArtistsList(
        this,
        { it.artists },
        { it.name }
    )
}

fun TrackItem.getArtistList() : String {
    return getArtistsList(
        this,
        { it.artists },
        { it.name }
    )
}

fun AlbumDetail.getArtistList() : String {
    return getArtistsList(
        this,
        { it.artists },
        { it.name }
    )
}

fun setupSearch(
    viewModel : AlbumViewModel,
    etSearch : EditText,
    lifecycleScope : LifecycleCoroutineScope,
    savedSearchQuery : String?
) {
    etSearch.doOnTextChanged { text, _, _, _ ->
        if (text.isNullOrEmpty()) {
            lifecycleScope.launch { viewModel.getSavedAlbums() }
        } else {
            lifecycleScope.launch { viewModel.searchAlbumByName(text.toString()) }
        }
    }
    savedSearchQuery?.let {
        etSearch.setText(it)
        lifecycleScope.launch { viewModel.searchAlbumByName(it) }
    }
}
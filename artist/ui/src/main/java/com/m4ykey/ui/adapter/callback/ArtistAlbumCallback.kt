package com.m4ykey.ui.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.album.ArtistAlbum

class ArtistAlbumCallback : DiffUtil.ItemCallback<ArtistAlbum>() {

    override fun areItemsTheSame(oldItem: ArtistAlbum, newItem: ArtistAlbum): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArtistAlbum, newItem: ArtistAlbum): Boolean {
        return oldItem == newItem
    }
}
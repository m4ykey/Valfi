package com.m4ykey.ui.album.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.album.AlbumItem

class AlbumCallback : DiffUtil.ItemCallback<AlbumItem>() {
    override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.albumType == newItem.albumType &&
                oldItem.name == newItem.name &&
                oldItem.images.toTypedArray().contentEquals(newItem.images.toTypedArray()) &&
                oldItem.artists.toTypedArray().contentEquals(newItem.artists.toTypedArray())
    }
}
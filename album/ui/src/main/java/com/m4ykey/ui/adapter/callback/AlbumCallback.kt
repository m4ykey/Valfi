package com.m4ykey.ui.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.album.AlbumItem

class AlbumCallback : DiffUtil.ItemCallback<AlbumItem>() {
    override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean =
        oldItem.id == newItem.id &&
                oldItem.name == newItem.name &&
                oldItem.albumType == newItem.albumType
}
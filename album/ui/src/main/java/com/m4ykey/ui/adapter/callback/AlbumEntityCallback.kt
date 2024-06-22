package com.m4ykey.ui.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.local.model.AlbumEntity

class AlbumEntityCallback : DiffUtil.ItemCallback<AlbumEntity>() {
    override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean =
        oldItem.id == newItem.id &&
                oldItem.name == newItem.name &&
                oldItem.images == newItem.images
}
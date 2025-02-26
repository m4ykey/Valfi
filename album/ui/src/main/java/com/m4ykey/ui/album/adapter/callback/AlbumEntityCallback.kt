package com.m4ykey.ui.album.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.local.model.AlbumEntity

class AlbumEntityCallback : DiffUtil.ItemCallback<AlbumEntity>() {
    override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean =
        oldItem.id == newItem.id && oldItem.saveTime == newItem.saveTime

    override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean {
        return oldItem.albumType == newItem.albumType &&
                oldItem.artists.toTypedArray().contentEquals(newItem.artists.toTypedArray()) &&
                oldItem.albumUrl == newItem.albumUrl &&
                oldItem.id == newItem.id &&
                oldItem.images == newItem.images &&
                oldItem.name == newItem.name &&
                oldItem.releaseDate == newItem.releaseDate &&
                oldItem.totalTracks == newItem.totalTracks &&
                oldItem.saveTime == newItem.saveTime
    }
}
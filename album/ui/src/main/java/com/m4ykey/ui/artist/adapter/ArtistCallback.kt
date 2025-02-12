package com.m4ykey.ui.artist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.album.Artist

class ArtistCallback : DiffUtil.ItemCallback<Artist>() {
    override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem == newItem
    }
}
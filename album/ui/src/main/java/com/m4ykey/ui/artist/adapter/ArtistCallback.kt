package com.m4ykey.ui.artist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.artist.Artist
import com.m4ykey.data.domain.model.artist.ArtistList

class ArtistCallback : DiffUtil.ItemCallback<ArtistList>() {
    override fun areItemsTheSame(oldItem: ArtistList, newItem: ArtistList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArtistList, newItem: ArtistList): Boolean {
        return oldItem == newItem
    }
}
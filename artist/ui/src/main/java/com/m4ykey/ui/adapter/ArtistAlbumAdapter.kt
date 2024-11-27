package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.artist.ui.databinding.LayoutArtistAlbumBinding
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.album.ArtistAlbum
import com.m4ykey.ui.adapter.callback.ArtistAlbumCallback
import com.m4ykey.ui.adapter.viewholder.ArtistAlbumViewHolder

class ArtistAlbumAdapter : BaseRecyclerView<ArtistAlbum, ArtistAlbumViewHolder>(ArtistAlbumCallback()) {

    override fun onItemBindViewHolder(
        holder: ArtistAlbumViewHolder,
        item: ArtistAlbum,
        position: Int
    ) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        val item = getItem(position)
        return item?.id?.toLong() ?: position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistAlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutArtistAlbumBinding.inflate(inflater, parent, false)
        return ArtistAlbumViewHolder(binding)
    }
}
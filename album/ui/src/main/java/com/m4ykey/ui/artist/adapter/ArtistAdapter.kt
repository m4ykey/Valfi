package com.m4ykey.ui.artist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.album.ui.databinding.LayoutArtistListBinding
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.artist.ArtistList

class ArtistAdapter(
    private val onArtistClick : OnArtistClick
) : BaseRecyclerView<ArtistList, ArtistViewHolder>(ArtistCallback()) {

    override fun onItemBindViewHolder(holder: ArtistViewHolder, item: ArtistList, position: Int) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        val item = differ.currentList.getOrNull(position)
        return item?.id?.toLong() ?: position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutArtistListBinding.inflate(inflater, parent, false)
        return ArtistViewHolder(binding, onArtistClick)
    }
}

typealias OnArtistClick = (ArtistList) -> Unit
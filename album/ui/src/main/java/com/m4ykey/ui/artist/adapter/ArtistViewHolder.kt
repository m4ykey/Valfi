package com.m4ykey.ui.artist.adapter

import com.m4ykey.album.ui.databinding.LayoutArtistListBinding
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.domain.model.artist.ArtistList

class ArtistViewHolder(
    binding : LayoutArtistListBinding,
    private val onArtistClick : OnArtistClick
) : BaseViewHolder<ArtistList, LayoutArtistListBinding>(binding) {

    override fun bind(item: ArtistList) {
        binding.apply {
            linearLayoutArtistList.setOnClickListener { onArtistClick(item) }
            txtArtist.text = item.name
            loadImage(imgArtist, item.images[0].url, imgArtist.context)
        }
    }
}
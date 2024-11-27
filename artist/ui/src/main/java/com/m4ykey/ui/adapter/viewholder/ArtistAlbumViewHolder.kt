package com.m4ykey.ui.adapter.viewholder

import com.m4ykey.artist.ui.databinding.LayoutArtistAlbumBinding
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.domain.model.album.ArtistAlbum
import com.m4ykey.ui.helpers.getLargestImageUrl

class ArtistAlbumViewHolder(
    binding : LayoutArtistAlbumBinding
) : BaseViewHolder<ArtistAlbum, LayoutArtistAlbumBinding>(binding) {

    override fun bind(item: ArtistAlbum) {
        with(binding) {
            loadImage(imgAlbum, item.getLargestImageUrl().toString(), imgAlbum.context)
            txtAlbumName.text = item.name
        }
    }
}
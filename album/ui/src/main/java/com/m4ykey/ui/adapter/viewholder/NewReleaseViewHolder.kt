package com.m4ykey.ui.adapter.viewholder

import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class NewReleaseViewHolder(
    binding : LayoutAlbumGridBinding,
    private val onAlbumClick : (AlbumItem) -> Unit
) : BaseViewHolder<AlbumItem, LayoutAlbumGridBinding>(binding) {

    override fun bind(item: AlbumItem) {
        with(binding) {
            layoutAlbum.setOnClickListener { onAlbumClick(item) }

            val image = item.images.maxByOrNull { it.height * it.width }?.url
            val artistList = item.artists.joinToString(", ") { it.name }
            loadImage(imgAlbum, image.toString(), imgAlbum.context)
            txtAlbum.text = item.name
            txtArtist.text = artistList
        }
    }
}
package com.m4ykey.ui.adapter.viewholder

import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.ui.helpers.OnAlbumClick

class NewReleaseViewHolder(
    binding : LayoutAlbumGridBinding,
    private val onAlbumClick : OnAlbumClick
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
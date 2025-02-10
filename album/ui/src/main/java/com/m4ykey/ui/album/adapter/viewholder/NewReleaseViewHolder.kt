package com.m4ykey.ui.album.adapter.viewholder

import com.m4ykey.album.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.album.helpers.OnAlbumClick
import com.m4ykey.ui.album.helpers.getArtistList
import com.m4ykey.ui.album.helpers.getLargestImageUrl

class NewReleaseViewHolder(
    binding : LayoutAlbumGridBinding,
    private val onAlbumClick : OnAlbumClick
) : BaseViewHolder<AlbumItem, LayoutAlbumGridBinding>(binding) {

    override fun bind(item: AlbumItem) {
        with(binding) {
            layoutAlbum.setOnClickListener { onAlbumClick(item) }

            loadImage(imgAlbum, item.getLargestImageUrl().toString(), imgAlbum.context)
            txtAlbum.text = item.name
            txtArtist.text = item.getArtistList()
        }
    }
}
package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.album.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.helpers.OnAlbumEntityClick
import com.m4ykey.ui.helpers.getArtistList

class AlbumGridViewHolder(
    binding : LayoutAlbumGridBinding,
    private val onAlbumClick : OnAlbumEntityClick
) : BaseViewHolder<AlbumEntity, LayoutAlbumGridBinding>(binding) {

    companion object {
        fun create(
            parent: ViewGroup,
            onAlbumClick: OnAlbumEntityClick
        ) : AlbumGridViewHolder {
            return AlbumGridViewHolder(
                binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onAlbumClick = onAlbumClick
            )
        }
    }

    override fun bind(item: AlbumEntity) {
        with(binding) {
            layoutAlbum.setOnClickListener { onAlbumClick(item) }

            txtArtist.text = item.getArtistList()
            txtAlbum.text = item.name
            loadImage(imgAlbum, item.images, imgAlbum.context)
        }
    }
}
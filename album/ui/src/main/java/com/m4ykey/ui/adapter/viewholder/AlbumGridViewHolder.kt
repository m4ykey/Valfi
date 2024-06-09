package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class AlbumGridViewHolder(
    binding : LayoutAlbumGridBinding,
    private val onAlbumClick : (AlbumEntity) -> Unit
) : BaseViewHolder<AlbumEntity, LayoutAlbumGridBinding>(binding) {

    companion object {
        fun create(
            parent: ViewGroup,
            onAlbumClick: (AlbumEntity) -> Unit
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

            txtArtist.text = item.artists.joinToString(", ") { it.name }
            txtAlbum.text = item.name
            loadImage(imgAlbum, item.images, imgAlbum.context)
        }
    }
}
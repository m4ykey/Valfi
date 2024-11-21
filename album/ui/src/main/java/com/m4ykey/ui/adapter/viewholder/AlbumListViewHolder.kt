package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.album.ui.databinding.LayoutAlbumListBinding
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.helpers.OnAlbumEntityClick
import com.m4ykey.ui.helpers.getArtistList

class AlbumListViewHolder(
    binding : LayoutAlbumListBinding,
    private val onAlbumClick : OnAlbumEntityClick
) : BaseViewHolder<AlbumEntity, LayoutAlbumListBinding>(binding) {

    companion object {
        fun create(
            parent : ViewGroup,
            onAlbumClick: OnAlbumEntityClick
        ) : AlbumListViewHolder {
            return AlbumListViewHolder(
                onAlbumClick = onAlbumClick,
                binding = LayoutAlbumListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
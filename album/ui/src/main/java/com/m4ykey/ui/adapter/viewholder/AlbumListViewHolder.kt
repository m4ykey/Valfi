package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.loadImage
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.databinding.LayoutAlbumListBinding

class AlbumListViewHolder(
    private val binding : LayoutAlbumListBinding,
    private val onAlbumClick : (AlbumEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent : ViewGroup,
            onAlbumClick: (AlbumEntity) -> Unit
        ) : AlbumListViewHolder {
            return AlbumListViewHolder(
                onAlbumClick = onAlbumClick,
                binding = LayoutAlbumListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    fun bind(item: AlbumEntity) {
        with(binding) {
            layoutAlbum.setOnClickListener { onAlbumClick(item) }

            txtArtist.text = item.artists
            txtAlbum.text = item.name
            loadImage(imgAlbum, item.images, imgAlbum.context)
        }
    }
}
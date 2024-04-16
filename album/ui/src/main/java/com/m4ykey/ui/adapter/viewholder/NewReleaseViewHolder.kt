package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.loadImage
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class NewReleaseViewHolder(
    private val binding : LayoutAlbumGridBinding,
    private val onAlbumClick : (AlbumItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup,
            onAlbumClick: (AlbumItem) -> Unit
        ) : NewReleaseViewHolder {
            return NewReleaseViewHolder(
                onAlbumClick = onAlbumClick,
                binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    fun bind(item: AlbumItem) {
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
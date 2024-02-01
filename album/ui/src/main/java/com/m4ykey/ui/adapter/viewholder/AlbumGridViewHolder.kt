package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class AlbumGridViewHolder(
    private val binding : LayoutAlbumGridBinding,
    private val listener : OnAlbumClick
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view : ViewGroup, listener : OnAlbumClick) : AlbumGridViewHolder {
            return AlbumGridViewHolder(
                binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(view.context), view, false),
                listener = listener
            )
        }
    }

    init {
        binding.root.setOnClickListener {
            listener.onAlbumClick(albumId)
        }
    }

    private var albumId = ""

    fun bind(album : AlbumEntity) {
        albumId = album.id
        with(binding) {
            imgAlbum.load(album.image)
            txtAlbum.text = album.name
            txtArtist.text = album.artistList
        }
    }

}
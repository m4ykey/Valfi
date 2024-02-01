package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.databinding.LayoutAlbumListBinding

class AlbumListViewHolder(
    private val binding : LayoutAlbumListBinding,
    private val listener : OnAlbumClick
) : RecyclerView.ViewHolder(binding.root) {

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

    companion object {
        fun create(view : ViewGroup, listener : OnAlbumClick) : AlbumListViewHolder {
            return AlbumListViewHolder(
                listener = listener,
                binding = LayoutAlbumListBinding.inflate(LayoutInflater.from(view.context), view, false)
            )
        }
    }

}
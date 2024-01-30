package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.databinding.LayoutAlbumBinding

class AlbumEntityViewHolder(
    private val binding : LayoutAlbumBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view : ViewGroup) : AlbumEntityViewHolder {
            return AlbumEntityViewHolder(binding = LayoutAlbumBinding.inflate(LayoutInflater.from(view.context), view, false))
        }
    }

    fun bind(album : AlbumEntity) {
        with(binding) {
            imgAlbum.load(album.image)
            txtAlbum.text = album.name
            txtArtist.text = album.artistList
        }
    }

}
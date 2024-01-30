package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class AlbumEntityGridViewHolder(
    private val binding : LayoutAlbumGridBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view : ViewGroup) : AlbumEntityGridViewHolder {
            return AlbumEntityGridViewHolder(binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(view.context), view, false))
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
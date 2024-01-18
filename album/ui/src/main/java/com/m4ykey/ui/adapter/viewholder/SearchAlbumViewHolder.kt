package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.data.domain.model.AlbumItem
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.databinding.LayoutAlbumBinding

class SearchAlbumViewHolder(
    private val binding: LayoutAlbumBinding,
    private val listener : OnAlbumClick
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view : ViewGroup, listener: OnAlbumClick) : SearchAlbumViewHolder {
            val inflater = LayoutInflater.from(view.context)
            return SearchAlbumViewHolder(
                listener = listener,
                binding = LayoutAlbumBinding.inflate(inflater, view, false)
            )
        }
    }

    init {
        binding.root.setOnClickListener {
            listener.onAlbumClick(albumId)
        }
    }

    private var albumId = ""

    fun bind(album : AlbumItem) {
        albumId = album.id
        with(binding) {
            val image = album.images.maxByOrNull { it.height * it.width }?.url
            val artistList = album.artists.joinToString(", ") { it.name }
            imgAlbum.load(image) {
                crossfade(true)
                crossfade(500)
            }
            txtAlbum.text = album.name
            txtArtist.text = artistList
        }
    }
}
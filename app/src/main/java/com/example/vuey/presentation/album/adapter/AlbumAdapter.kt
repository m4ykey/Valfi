package com.example.vuey.presentation.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.presentation.album.AlbumFragmentDirections
import com.example.vuey.presentation.album.SearchAlbumFragmentDirections
import com.m4ykey.common.utils.DiffUtils
import com.m4ykey.common.utils.toAlbumEntity
import com.m4ykey.common.utils.toListenLaterEntity
import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.remote.album.model.spotify.album.AlbumList

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albums = emptyList<Any>()

    fun submitAlbums(newData : List<Any>) {
        val oldData = albums.toList()
        albums = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class AlbumViewHolder(private val binding: LayoutAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album : Any) {
            with(binding) {
                when(album) {
                    is AlbumList -> {
                        val image = album.imageList.find { it.height == 640 && it.width == 640 }
                        txtAlbum.text = album.albumName
                        txtArtist.text = album.artistList.joinToString(separator = ", ") { it.artistName }
                        imgAlbum.load(image?.url) { error(R.drawable.album_error) }

                        layoutAlbum.setOnClickListener {
                            val action = SearchAlbumFragmentDirections.actionSearchAlbumFragmentToAlbumDetailFragment(
                                albumEntity = album.toAlbumEntity(),
                                listenLaterEntity = album.toListenLaterEntity(),
                                albumId = album.id
                            )
                            it.findNavController().navigate(action)
                        }
                    }
                    is AlbumEntity -> {
                        imgAlbum.load(album.albumCover.url) { error(R.drawable.album_error) }
                        txtAlbum.text = album.albumName
                        txtArtist.text = album.artistList.joinToString(separator = ", ") { it.name }
                        layoutAlbum.setOnClickListener {
                            val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(
                                albumEntity = album,
                                listenLaterEntity = album.toListenLaterEntity(),
                                albumId = album.id
                            )
                            it.findNavController().navigate(action)
                        }
                    }
                    else -> return
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = LayoutAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }
}
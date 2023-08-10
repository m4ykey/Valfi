package com.example.vuey.feature_album.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.core.common.utils.DiffUtils
import com.example.vuey.core.common.utils.toAlbum
import com.example.vuey.core.common.utils.toListenLaterEntity
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.feature_album.data.local.source.entity.AlbumEntity
import com.example.vuey.feature_album.presentation.ui.AlbumFragmentDirections

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albums = emptyList<AlbumEntity>()

    fun submitAlbums(newData : List<AlbumEntity>) {
        val oldData = albums.toList()
        albums = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class AlbumViewHolder(private val binding: LayoutAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album : AlbumEntity) {
            with(binding) {
                val image = album.albumCover.copy(
                    height = 640,
                    width = 640,
                    url = album.albumCover.url
                )
                imgAlbum.load(image.url) { error(R.drawable.album_error) }
                txtAlbum.text = album.albumName
                txtArtist.text = album.artistList.joinToString(separator = ", ") { it.name }
                layoutAlbum.setOnClickListener {
                    val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(
                        album = album.toAlbum(),
                        albumEntity = album,
                        listenLaterEntity = album.toListenLaterEntity(),
                        albumId = album.id,
                        isFromAlbumListenLaterFragment = false
                    )
                    it.findNavController().navigate(action)
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
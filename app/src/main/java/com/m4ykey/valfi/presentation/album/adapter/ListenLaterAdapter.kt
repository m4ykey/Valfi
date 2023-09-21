package com.m4ykey.valfi.presentation.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.common.utils.DiffUtils
import com.m4ykey.common.utils.toAlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
import com.m4ykey.valfi.databinding.LayoutListenLaterBinding
import com.m4ykey.valfi.presentation.album.AlbumListenLaterFragmentDirections

class ListenLaterAdapter : RecyclerView.Adapter<ListenLaterAdapter.ListenLaterViewHolder>() {

    private var albums = listOf<ListenLaterEntity>()

    fun submitAlbum(newData : List<ListenLaterEntity>) {
        val oldData = albums.toList()
        albums = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class ListenLaterViewHolder(private val binding : LayoutListenLaterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album : ListenLaterEntity) {
            with(binding) {
                txtAlbumTitle.text = album.albumTitle
                imgAlbum.load(album.albumImage.url) {
                    crossfade(true)
                    crossfade(500)
                }
                layoutAlbum.setOnClickListener {
                    val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(
                        albumEntity = album.toAlbumEntity(),
                        listenLaterEntity = album,
                        albumId = album.albumId
                    )
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListenLaterViewHolder {
        val binding = LayoutListenLaterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListenLaterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: ListenLaterViewHolder, position: Int) {
        holder.bind(albums[position])
    }

}
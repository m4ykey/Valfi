package com.example.vuey.feature_album.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.core.common.utils.DiffUtils
import com.example.vuey.databinding.LayoutListenLaterBinding
import com.example.vuey.feature_album.data.local.source.entity.ListenLaterEntity

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
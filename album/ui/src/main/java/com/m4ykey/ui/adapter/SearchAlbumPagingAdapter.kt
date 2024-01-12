package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.data.domain.model.AlbumItem
import com.m4ykey.ui.databinding.LayoutAlbumBinding

class SearchAlbumPagingAdapter : PagingDataAdapter<AlbumItem, SearchAlbumPagingAdapter.AlbumViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        getItem(position)?.let { album ->
            holder.bind(album)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            LayoutAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class AlbumViewHolder(private val binding: LayoutAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album : AlbumItem) {
            with(binding) {
                txtAlbum.text = album.name
            }
        }
    }

}
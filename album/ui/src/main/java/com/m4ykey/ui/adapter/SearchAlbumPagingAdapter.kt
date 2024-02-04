package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.adapter.viewholder.SearchAlbumViewHolder

class SearchAlbumPagingAdapter(private val listener : OnItemClickListener<AlbumItem>) : PagingDataAdapter<AlbumItem, SearchAlbumViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: SearchAlbumViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAlbumViewHolder {
        return SearchAlbumViewHolder.create(parent, listener)
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem == newItem
        }
    }

}
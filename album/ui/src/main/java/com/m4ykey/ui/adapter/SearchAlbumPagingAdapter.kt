package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.AlbumViewModel
import com.m4ykey.ui.adapter.viewholder.SearchAlbumViewHolder

class SearchAlbumPagingAdapter(
    private val onAlbumClick : (AlbumItem) -> Unit,
    private val viewModel: AlbumViewModel
) : PagingDataAdapter<AlbumItem, SearchAlbumViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: SearchAlbumViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAlbumViewHolder {
        return SearchAlbumViewHolder.create(parent, onAlbumClick, viewModel)
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem == newItem
        }
    }
}
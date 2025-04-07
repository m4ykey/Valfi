package com.m4ykey.ui.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.m4ykey.album.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.album.adapter.callback.AlbumCallback
import com.m4ykey.ui.album.adapter.viewholder.SearchAlbumViewHolder
import com.m4ykey.ui.album.helpers.OnAlbumClick

class SearchAlbumAdapter(
    private val onAlbumClick : OnAlbumClick
) : PagingDataAdapter<AlbumItem, SearchAlbumViewHolder>(AlbumCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumGridBinding.inflate(inflater, parent, false)
        return SearchAlbumViewHolder(binding, onAlbumClick)
    }

    override fun onBindViewHolder(holder: SearchAlbumViewHolder, position: Int) {
        val album = getItem(position)
        album?.let { holder.bind(it) }
    }
}
package com.m4ykey.ui.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.album.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.album.adapter.callback.AlbumCallback
import com.m4ykey.ui.album.adapter.viewholder.SearchAlbumViewHolder
import com.m4ykey.ui.album.helpers.OnAlbumClick

class SearchAlbumAdapter(
    private val onAlbumClick : OnAlbumClick
) : BaseRecyclerView<AlbumItem, SearchAlbumViewHolder>(AlbumCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onItemBindViewHolder(holder: SearchAlbumViewHolder, item: AlbumItem, position: Int) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumGridBinding.inflate(inflater, parent, false)
        return SearchAlbumViewHolder(binding, onAlbumClick)
    }

    override fun getItemForPosition(position: Int): Long {
        return differ.currentList.getOrNull(position)?.longId ?: RecyclerView.NO_ID
    }
}
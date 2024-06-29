package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.m4ykey.core.views.animations.applyAnimation
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.callback.AlbumCallback
import com.m4ykey.ui.adapter.viewholder.SearchAlbumViewHolder
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.ui.helpers.OnAlbumClick

class SearchAlbumAdapter(
    private val onAlbumClick : OnAlbumClick
) : BaseRecyclerView<AlbumItem, SearchAlbumViewHolder>(AlbumCallback()) {

    private var lastVisibleItemPosition = -1

    override fun onItemBindViewHolder(holder: SearchAlbumViewHolder, position: Int) {
        val item = currentList[position]
        item?.let {
            holder.bind(it)
            holder.applyAnimation(position, lastVisibleItemPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumGridBinding.inflate(inflater, parent, false)
        return SearchAlbumViewHolder(binding, onAlbumClick)
    }

    override fun getItemForPosition(position: Int): Long {
        val item = getItem(position)
        return item?.id?.toLong() ?: position.toLong()
    }
}
package com.m4ykey.ui.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.album.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.album.adapter.callback.AlbumCallback
import com.m4ykey.ui.album.adapter.viewholder.NewReleaseViewHolder
import com.m4ykey.ui.album.helpers.OnAlbumClick

class NewReleaseAdapter(
    private val onAlbumClick : OnAlbumClick
) : BaseRecyclerView<AlbumItem, NewReleaseViewHolder>(AlbumCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewReleaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumGridBinding.inflate(inflater, parent, false)
        return NewReleaseViewHolder(binding, onAlbumClick)
    }

    override fun onItemBindViewHolder(holder: NewReleaseViewHolder, item : AlbumItem, position: Int) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        return differ.currentList.getOrNull(position)?.longId ?: RecyclerView.NO_ID
    }
}
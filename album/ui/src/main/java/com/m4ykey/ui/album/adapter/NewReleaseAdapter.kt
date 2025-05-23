package com.m4ykey.ui.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.m4ykey.album.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.album.adapter.callback.AlbumCallback
import com.m4ykey.ui.album.adapter.viewholder.NewReleaseViewHolder
import com.m4ykey.ui.album.helpers.OnAlbumClick

class NewReleaseAdapter(
    private val onAlbumClick : OnAlbumClick
) : PagingDataAdapter<AlbumItem, NewReleaseViewHolder>(AlbumCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewReleaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumGridBinding.inflate(inflater, parent, false)
        return NewReleaseViewHolder(binding, onAlbumClick)
    }

    override fun onBindViewHolder(holder: NewReleaseViewHolder, position: Int) {
        val album = getItem(position)
        album?.let { holder.bind(it) }
    }
}
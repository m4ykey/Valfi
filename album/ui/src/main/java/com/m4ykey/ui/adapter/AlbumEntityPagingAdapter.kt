package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.viewholder.AlbumEntityViewHolder

class AlbumEntityPagingAdapter : PagingDataAdapter<AlbumEntity, AlbumEntityViewHolder>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumEntity>() {
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: AlbumEntityViewHolder, position: Int) {
        getItem(position)?.let { album ->
            holder.bind(album)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumEntityViewHolder {
        return AlbumEntityViewHolder.create(parent)
    }

}
package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.viewholder.AlbumEntityGridViewHolder
import com.m4ykey.ui.adapter.viewholder.AlbumEntityListViewHolder

class AlbumEntityPagingAdapter : PagingDataAdapter<AlbumEntity, RecyclerView.ViewHolder>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumEntity>() {
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem == newItem
        }
    }

    enum class ViewType {
        LIST,
        GRID
    }

    var currentViewType = ViewType.GRID

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumEntityListViewHolder -> getItem(position)?.let { album -> holder.bind(album) }
            is AlbumEntityGridViewHolder -> getItem(position)?.let { album -> holder.bind(album) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (currentViewType) {
            ViewType.GRID -> AlbumEntityGridViewHolder.create(parent)
            ViewType.LIST -> AlbumEntityListViewHolder.create(parent)
        }
    }

    fun setupViewType(viewType: ViewType) {
        currentViewType = viewType
        notifyDataSetChanged()
    }

}
package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.viewholder.AlbumGridViewHolder
import com.m4ykey.ui.adapter.viewholder.AlbumListViewHolder
import com.m4ykey.ui.helpers.ViewType

class AlbumEntityPagingAdapter(private val listener : OnItemClickListener<AlbumEntity>) : PagingDataAdapter<AlbumEntity, RecyclerView.ViewHolder>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumEntity>() {
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem == newItem
        }
    }

    var currentViewType = ViewType.GRID

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumGridViewHolder -> getItem(position)?.let { album -> holder.bind(album) }
            is AlbumListViewHolder -> getItem(position)?.let { album -> holder.bind(album) }
            else -> throw IllegalArgumentException("Unknown view holder type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.GRID.ordinal -> AlbumGridViewHolder.create(parent, listener)
            ViewType.LIST.ordinal -> AlbumListViewHolder.create(parent, listener)
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentViewType.ordinal
    }

    fun setupViewType(viewType: ViewType) {
        val previousViewType = currentViewType
        currentViewType = viewType

        when (previousViewType) {
            ViewType.GRID -> {
                when (viewType) {
                    ViewType.LIST -> notifyItemRangeChanged(0, itemCount)
                    else -> {}
                }
            }
            ViewType.LIST -> {
                when (viewType) {
                    ViewType.GRID -> notifyItemRangeChanged(0, itemCount)
                    else -> {}
                }
            }
        }
    }

}
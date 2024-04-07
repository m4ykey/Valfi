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

class AlbumPagingAdapter(
    private val listener: OnItemClickListener<AlbumEntity>
) : PagingDataAdapter<AlbumEntity, RecyclerView.ViewHolder>(COMPARATOR) {

    var viewType : ViewType = ViewType.GRID

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumEntity>() {
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem == newItem
        }

        private const val VIEW_TYPE_GRID = 0
        private const val VIEW_TYPE_LIST = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumGridViewHolder -> holder.bind(getItem(position) ?: return)
            is AlbumListViewHolder -> holder.bind(getItem(position) ?: return)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID -> AlbumGridViewHolder.create(parent, listener)
            VIEW_TYPE_LIST -> AlbumListViewHolder.create(parent, listener)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            ViewType.GRID -> VIEW_TYPE_GRID
            ViewType.LIST -> VIEW_TYPE_LIST
        }
    }
}
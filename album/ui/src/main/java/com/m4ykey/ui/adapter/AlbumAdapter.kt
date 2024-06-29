package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.core.views.sorting.ViewType
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.callback.AlbumEntityCallback
import com.m4ykey.ui.adapter.viewholder.AlbumGridViewHolder
import com.m4ykey.ui.adapter.viewholder.AlbumListViewHolder
import com.m4ykey.ui.helpers.OnAlbumEntityClick

class AlbumAdapter(
    private val onAlbumClick : OnAlbumEntityClick
) : BaseRecyclerView<AlbumEntity, RecyclerView.ViewHolder>(AlbumEntityCallback()) {

    var viewType : ViewType = ViewType.GRID

    companion object {
        private const val VIEW_TYPE_GRID = 0
        private const val VIEW_TYPE_LIST = 1
    }

    override fun onItemBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val album = currentList[position]
        when (holder) {
            is AlbumGridViewHolder -> holder.bind(album)
            is AlbumListViewHolder -> holder.bind(album)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID -> AlbumGridViewHolder.create(parent, onAlbumClick)
            VIEW_TYPE_LIST -> AlbumListViewHolder.create(parent, onAlbumClick)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            ViewType.GRID -> VIEW_TYPE_GRID
            ViewType.LIST -> VIEW_TYPE_LIST
        }
    }

    override fun getItemForPosition(position: Int): Long {
        val item = getItem(position)
        return item?.id?.toLong() ?: position.toLong()
    }
}
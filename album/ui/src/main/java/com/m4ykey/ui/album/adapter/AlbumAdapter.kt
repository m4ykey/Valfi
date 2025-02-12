package com.m4ykey.ui.album.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.core.views.sorting.ViewType
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.album.adapter.callback.AlbumEntityCallback
import com.m4ykey.ui.album.adapter.viewholder.AlbumGridViewHolder
import com.m4ykey.ui.album.adapter.viewholder.AlbumListViewHolder
import com.m4ykey.ui.album.helpers.OnAlbumEntityClick

class AlbumAdapter(
    private val onAlbumClick : OnAlbumEntityClick
) : BaseRecyclerView<AlbumEntity, RecyclerView.ViewHolder>(AlbumEntityCallback()) {

    var viewType : ViewType = ViewType.GRID

    companion object {
        private const val VIEW_TYPE_GRID = 0
        private const val VIEW_TYPE_LIST = 1
    }

    init {
        setHasStableIds(true)
    }

    override fun onItemBindViewHolder(holder: RecyclerView.ViewHolder, item: AlbumEntity, position: Int) {
        (holder as? AlbumGridViewHolder)?.bind(item)
        (holder as? AlbumListViewHolder)?.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID -> AlbumGridViewHolder.create(parent, onAlbumClick)
            VIEW_TYPE_LIST -> AlbumListViewHolder.create(parent, onAlbumClick)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int = viewType.ordinal
//        return when (viewType) {
//            ViewType.GRID -> VIEW_TYPE_GRID
//            ViewType.LIST -> VIEW_TYPE_LIST
//        }


    override fun getItemForPosition(position: Int): Long {
        return differ.currentList.getOrNull(position)?.id?.hashCode()?.toLong() ?: RecyclerView.NO_ID
    }
}
package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.sorting.ViewType
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.viewholder.AlbumGridViewHolder
import com.m4ykey.ui.adapter.viewholder.AlbumListViewHolder

class AlbumAdapter(
    private val onAlbumClick : (AlbumEntity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewType : ViewType = ViewType.GRID

    private val asyncListDiffer = AsyncListDiffer(this, COMPARATOR)

    fun submitList(albums : List<AlbumEntity>) {
        asyncListDiffer.submitList(albums)
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumEntity>() {
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean = oldItem == newItem
        }

        private const val VIEW_TYPE_GRID = 0
        private const val VIEW_TYPE_LIST = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID -> AlbumGridViewHolder.create(parent, onAlbumClick)
            VIEW_TYPE_LIST -> AlbumListViewHolder.create(parent, onAlbumClick)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val album = asyncListDiffer.currentList[position]
        when (holder) {
            is AlbumGridViewHolder -> holder.bind(album)
            is AlbumListViewHolder -> holder.bind(album)
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            ViewType.GRID -> VIEW_TYPE_GRID
            ViewType.LIST -> VIEW_TYPE_LIST
        }
    }

}
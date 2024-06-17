package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.viewholder.SearchAlbumViewHolder
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.ui.helpers.OnAlbumClick

class SearchAlbumAdapter(
    private val onAlbumClick : OnAlbumClick
) : RecyclerView.Adapter<SearchAlbumViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, COMPARATOR)

    fun submitList(albums : List<AlbumItem>) {
        asyncListDiffer.submitList(albums)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumGridBinding.inflate(inflater, parent, false)
        return SearchAlbumViewHolder(binding, onAlbumClick)
    }

    override fun onBindViewHolder(holder: SearchAlbumViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        item?.let {
            holder.bind(it)
            if (holder.itemView.animation == null) {
                val fadeIn = AlphaAnimation(0f, 1f)
                fadeIn.duration = 300
                holder.itemView.animation = fadeIn
            }
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem == newItem
        }
    }
}
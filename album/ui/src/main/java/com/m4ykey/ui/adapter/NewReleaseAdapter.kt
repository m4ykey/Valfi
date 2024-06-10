package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.viewholder.NewReleaseViewHolder
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class NewReleaseAdapter(
    private val onAlbumClick : (AlbumItem) -> Unit
) : RecyclerView.Adapter<NewReleaseViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, COMPARATOR)

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewReleaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumGridBinding.inflate(inflater, parent, false)
        return NewReleaseViewHolder(binding, onAlbumClick)
    }

    override fun onBindViewHolder(holder: NewReleaseViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun submitList(albums : List<AlbumItem>) {
        asyncListDiffer.submitList(albums)
    }

}
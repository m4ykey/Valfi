package com.m4ykey.core.views.recyclerview

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView<Item, VH : RecyclerView.ViewHolder>(
    diffCallback : DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, VH>(diffCallback) {

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        onItemBindViewHolder(holder, position)
    }

    abstract fun onItemBindViewHolder(holder: VH, position: Int)

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        submitList(emptyList())
    }

    override fun getItemId(position: Int): Long {
        return getItemForPosition(position)
    }

    abstract fun getItemForPosition(position: Int) : Long

}
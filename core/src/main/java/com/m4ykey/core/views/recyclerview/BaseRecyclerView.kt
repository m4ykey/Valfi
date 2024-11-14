package com.m4ykey.core.views.recyclerview

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.recyclerview.animations.applyAnimation

abstract class BaseRecyclerView<Item, VH : RecyclerView.ViewHolder>(
    diffCallback : DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, VH>(diffCallback) {

    private var lastVisibleItemPosition = -1

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(holder.bindingAdapterPosition)
        item?.let {
            onItemBindViewHolder(holder, it, position)
            holder.applyAnimation(position, lastVisibleItemPosition)
            lastVisibleItemPosition = holder.bindingAdapterPosition
        }
    }

    abstract fun onItemBindViewHolder(holder: VH, item : Item, position: Int)

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        submitList(emptyList())
    }

    override fun getItemId(position: Int): Long {
        return getItemForPosition(position)
    }

    abstract fun getItemForPosition(position: Int) : Long

}
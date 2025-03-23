package com.m4ykey.core.views.recyclerview

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.recyclerview.animations.applyAnimation

abstract class BaseRecyclerView<Item, VH : RecyclerView.ViewHolder>(
    diffCallback : DiffUtil.ItemCallback<Item>
) : RecyclerView.Adapter<VH>() {

    val differ : AsyncListDiffer<Item> by lazy {
        AsyncListDiffer(AdapterListUpdateCallback(this), AsyncDifferConfig.Builder(diffCallback).build())
    }
    private var lastVisibleItemPosition = -1

    fun submitList(list : List<Item>, isAppend : Boolean = false) {
        val differCurrentList = differ.currentList
        if (isAppend && list.size == differCurrentList.size) return

        val newList = if (isAppend) differCurrentList.toMutableList().apply { addAll(list) } else list.toList()
        differ.submitList(newList)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = differ.currentList[position]
        item?.let {
            onItemBindViewHolder(holder, it, position)
            holder.applyAnimation(position, lastVisibleItemPosition)
            lastVisibleItemPosition = holder.adapterPosition
        }
    }

    abstract fun onItemBindViewHolder(holder: VH, item : Item, position: Int)

    override fun getItemId(position: Int): Long {
        return getItemForPosition(position).takeIf { it != RecyclerView.NO_ID } ?: position.toLong()
    }

    abstract fun getItemForPosition(position: Int) : Long

}
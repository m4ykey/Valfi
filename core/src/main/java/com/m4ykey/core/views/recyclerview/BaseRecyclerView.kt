package com.m4ykey.core.views.recyclerview

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView<Item, VH : RecyclerView.ViewHolder>(
    diffCallback : DiffUtil.ItemCallback<Item>
) : RecyclerView.Adapter<VH>() {

    val differ : AsyncListDiffer<Item> by lazy {
        AsyncListDiffer(AdapterListUpdateCallback(this), AsyncDifferConfig.Builder(diffCallback).build())
    }

    fun submitList(list : List<Item>) {
        differ.submitList(list)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = differ.currentList[position]
        onItemBindViewHolder(holder, item, position)
    }

    abstract fun onItemBindViewHolder(holder: VH, item : Item, position: Int)

    override fun getItemId(position: Int): Long {
        return getItemForPosition(position).takeIf { it != RecyclerView.NO_ID } ?: position.toLong()
    }

    abstract fun getItemForPosition(position: Int) : Long
}
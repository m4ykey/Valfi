package com.m4ykey.core.views.recyclerview

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView<Item, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract val asyncListDiffer : AsyncListDiffer<Item>

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun submitList(items : List<Item>) {
        asyncListDiffer.submitList(items)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onItemBindViewHolder(holder, position)
    }

    abstract fun onItemBindViewHolder(holder: VH, position: Int)

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        submitList(emptyList())
    }

}
package com.m4ykey.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.local.model.ListenLaterEntity
import com.m4ykey.ui.adapter.viewholder.ListenLaterViewHolder

class ListenLaterPagingAdapter(
    private val listener : OnItemClickListener<ListenLaterEntity>,
    private val context : Context
) : PagingDataAdapter<ListenLaterEntity, ListenLaterViewHolder>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ListenLaterEntity>() {
            override fun areItemsTheSame(oldItem: ListenLaterEntity, newItem: ListenLaterEntity): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ListenLaterEntity, newItem: ListenLaterEntity): Boolean = oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ListenLaterViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListenLaterViewHolder {
        return ListenLaterViewHolder.create(parent, listener, context)
    }
}
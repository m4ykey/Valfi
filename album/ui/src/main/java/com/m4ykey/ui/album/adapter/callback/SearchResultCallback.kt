package com.m4ykey.ui.album.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.local.model.SearchResult

class SearchResultCallback : DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem.id == newItem.id && oldItem.name == newItem.name
    }
}
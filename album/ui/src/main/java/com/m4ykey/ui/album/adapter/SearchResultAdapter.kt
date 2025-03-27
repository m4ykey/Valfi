package com.m4ykey.ui.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.album.ui.databinding.LayoutSearchResultBinding
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.local.model.SearchResult
import com.m4ykey.ui.album.adapter.callback.SearchResultCallback
import com.m4ykey.ui.album.adapter.viewholder.SearchResultViewHolder
import com.m4ykey.ui.album.helpers.OnSearchClick

class SearchResultAdapter(
    private val onSearchClick: OnSearchClick
) : BaseRecyclerView<SearchResult, SearchResultViewHolder>(SearchResultCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onItemBindViewHolder(
        holder: SearchResultViewHolder,
        item: SearchResult,
        position: Int
    ) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        return differ.currentList.getOrNull(position)?.id?.toLong() ?: position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutSearchResultBinding.inflate(inflater, parent, false)
        return SearchResultViewHolder(binding, onSearchClick)
    }
}
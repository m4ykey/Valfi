package com.m4ykey.ui.adapter.viewholder

import com.m4ykey.album.ui.databinding.LayoutSearchResultBinding
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.local.model.SearchResult
import com.m4ykey.ui.helpers.OnSearchClick

class SearchResultViewHolder(
    binding : LayoutSearchResultBinding,
    private val onSearchClick : OnSearchClick
) : BaseViewHolder<SearchResult, LayoutSearchResultBinding>(binding) {

    override fun bind(item: SearchResult) {
        binding.searchResult.setOnClickListener { onSearchClick(item) }
        binding.txtSearchResult.text = item.name
    }
}
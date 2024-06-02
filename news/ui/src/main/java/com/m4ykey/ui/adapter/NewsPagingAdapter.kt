package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.Article
import com.m4ykey.ui.adapter.viewholder.NewsViewHolder

class NewsPagingAdapter(
    private val onNewsClick : (Article) -> Unit
) : PagingDataAdapter<Article, NewsViewHolder>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url
            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.create(parent, onNewsClick)
    }
}
package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.Article
import com.m4ykey.ui.ListType
import com.m4ykey.ui.adapter.callback.ArticleCallback
import com.m4ykey.ui.adapter.viewholder.NewsListViewHolder
import com.m4ykey.ui.adapter.viewholder.NewsTableViewHolder

class NewsAdapter(
    private val onNewsClick : (Article) -> Unit
) : BaseRecyclerView<Article, RecyclerView.ViewHolder>(ArticleCallback()) {

    var listType : ListType = ListType.TABLE

    companion object {
        private const val LIST_TYPE = 0
        private const val TABLE_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when (viewType) {
           LIST_TYPE -> NewsListViewHolder.create(parent, onNewsClick)
           TABLE_TYPE -> NewsTableViewHolder.create(parent, onNewsClick)
           else -> throw IllegalArgumentException("Invalid view type")
       }
    }

    override fun onItemBindViewHolder(holder: RecyclerView.ViewHolder, item: Article, position: Int) {
        val news = currentList[position]
        when (holder) {
            is NewsListViewHolder -> holder.bind(news)
            is NewsTableViewHolder -> holder.bind(news)
        }
    }

    override fun getItemForPosition(position: Int): Long {
        val item = getItem(position)
        return item?.url?.hashCode()?.toLong() ?: position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return when (listType) {
            ListType.TABLE -> TABLE_TYPE
            ListType.LIST -> LIST_TYPE
        }
    }
}
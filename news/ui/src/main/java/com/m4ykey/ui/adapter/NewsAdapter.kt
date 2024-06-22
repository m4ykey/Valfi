package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.core.views.animations.applyAnimation
import com.m4ykey.data.domain.model.Article
import com.m4ykey.ui.adapter.callback.ArticleCallback
import com.m4ykey.ui.adapter.viewholder.NewsViewHolder
import com.m4ykey.ui.databinding.LayoutNewsListBinding

class NewsAdapter(
    private val onNewsClick : (Article) -> Unit
) : BaseRecyclerView<Article, NewsViewHolder>() {

    override val asyncListDiffer = AsyncListDiffer(this, ArticleCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutNewsListBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding, onNewsClick)
    }

    override fun onItemBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        item?.let {
            holder.bind(it)
            applyAnimation(holder.itemView)
        }
    }
}
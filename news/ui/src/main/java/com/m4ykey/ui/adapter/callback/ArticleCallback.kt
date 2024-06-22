package com.m4ykey.ui.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.Article

class ArticleCallback : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.url == newItem.url &&
                oldItem.content == newItem.content &&
                oldItem.title == newItem.title &&
                oldItem.urlToImage == newItem.urlToImage
}
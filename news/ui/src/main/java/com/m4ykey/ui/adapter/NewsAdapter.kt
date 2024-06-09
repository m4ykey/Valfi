package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.data.domain.model.Article
import com.m4ykey.ui.adapter.viewholder.NewsViewHolder
import com.m4ykey.ui.databinding.LayoutNewsListBinding

class NewsAdapter(
    private val onNewsClick : (Article) -> Unit
) : RecyclerView.Adapter<NewsViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, COMPARATOR)

    fun submitList(articles : List<Article>) {
        asyncListDiffer.submitList(articles)
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url
            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutNewsListBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding, onNewsClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size
}
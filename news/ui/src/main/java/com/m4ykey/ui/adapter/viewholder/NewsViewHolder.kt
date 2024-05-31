package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.loadImage
import com.m4ykey.data.domain.model.Article
import com.m4ykey.ui.databinding.LayoutNewsListBinding

class NewsViewHolder(
    private val binding: LayoutNewsListBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup
        ) : NewsViewHolder {
            return NewsViewHolder(
                binding = LayoutNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    fun bind(item : Article) {
        with(binding) {
            loadImage(imgArticle, item.urlToImage, imgArticle.context)
            txtTitle.text = item.title
        }
    }

}
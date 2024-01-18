package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.ui.databinding.LayoutAlbumLoadStateBinding

class LoadStateViewHolder(private val binding : LayoutAlbumLoadStateBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view : ViewGroup) : LoadStateViewHolder {
            val inflater = LayoutInflater.from(view.context)
            return LoadStateViewHolder(LayoutAlbumLoadStateBinding.inflate(inflater, view, false))
        }
    }

    fun bind(loadState: LoadState) {
        binding.apply {
            progressBar.isVisible = loadState is LoadState.Loading
        }
    }
}
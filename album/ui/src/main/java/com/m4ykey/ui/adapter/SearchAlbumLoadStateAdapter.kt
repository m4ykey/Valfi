package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.ui.databinding.LayoutAlbumLoadStateBinding

class SearchAlbumLoadStateAdapter : LoadStateAdapter<SearchAlbumLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val binding : LayoutAlbumLoadStateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutAlbumLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


}
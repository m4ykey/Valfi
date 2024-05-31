package com.m4ykey.core.views.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.databinding.LayoutLoadStateBinding

class LoadStateViewHolder(
    private val binding : LayoutLoadStateBinding,
    private val retry : () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view : ViewGroup, retry: () -> Unit) : LoadStateViewHolder {
            return LoadStateViewHolder(
                binding = LayoutLoadStateBinding.inflate(LayoutInflater.from(view.context), view, false),
                retry = retry
            )
        }
    }

    fun bind(loadState: LoadState) {
        binding.apply {
            progressbar.isVisible = loadState is LoadState.Loading
            btnRetry.isVisible = loadState !is LoadState.Loading
            txtError.isVisible = loadState !is LoadState.Loading
        }
    }

    init {
        binding.btnRetry.setOnClickListener { retry.invoke() }
    }

}
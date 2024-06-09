package com.m4ykey.core.views.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T, VB : ViewBinding>(
    protected val binding : VB
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item : T)
}
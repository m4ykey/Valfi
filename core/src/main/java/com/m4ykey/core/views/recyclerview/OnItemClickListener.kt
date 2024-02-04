package com.m4ykey.core.views.recyclerview

interface OnItemClickListener<T> {
    fun onItemClick(position: Int, item: T)
}
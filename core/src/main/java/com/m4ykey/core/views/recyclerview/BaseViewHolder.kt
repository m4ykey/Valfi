package com.m4ykey.core.views.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(
    private val listener : OnItemClickListener<T>? = null,
    private val viewType : View
) : RecyclerView.ViewHolder(viewType) {

    abstract fun bind(item : T)

    init {
        listener?.let {
            viewType.setOnClickListener {
                listener.onItemClick(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    abstract fun getItem(position : Int) : T

}
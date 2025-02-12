package com.m4ykey.ui.album.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.ui.album.colors.ColorList

class ColorCallback : DiffUtil.ItemCallback<ColorList>() {
    override fun areItemsTheSame(oldItem: ColorList, newItem: ColorList): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ColorList, newItem: ColorList): Boolean {
        return oldItem.id == newItem.id && oldItem.color == newItem.color
    }
}
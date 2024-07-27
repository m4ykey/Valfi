package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.ui.adapter.callback.ColorCallback
import com.m4ykey.ui.adapter.viewholder.ColorViewHolder
import com.m4ykey.ui.colors.ColorList
import com.m4ykey.ui.databinding.LayoutColorsBinding
import com.m4ykey.ui.helpers.OnColorClick

class ColorAdapter(
    private val onColorClick : OnColorClick
) : BaseRecyclerView<ColorList, ColorViewHolder>(ColorCallback()){

    override fun onItemBindViewHolder(holder: ColorViewHolder, item: ColorList, position: Int) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        val item = getItem(position)
        return item?.id?.toLong() ?: position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutColorsBinding.inflate(inflater, parent, false)
        return ColorViewHolder(binding, onColorClick)
    }
}
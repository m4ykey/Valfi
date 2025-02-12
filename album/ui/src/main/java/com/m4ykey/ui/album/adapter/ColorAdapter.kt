package com.m4ykey.ui.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.album.ui.databinding.LayoutColorsBinding
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.ui.album.adapter.callback.ColorCallback
import com.m4ykey.ui.album.adapter.viewholder.ColorViewHolder
import com.m4ykey.ui.album.colors.ColorList
import com.m4ykey.ui.album.helpers.OnColorClick

class ColorAdapter(
    private val onColorClick : OnColorClick
) : BaseRecyclerView<ColorList, ColorViewHolder>(ColorCallback()){

    init {
        setHasStableIds(true)
    }

    override fun onItemBindViewHolder(holder: ColorViewHolder, item: ColorList, position: Int) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        return differ.currentList.getOrNull(position)?.id?.toLong() ?: RecyclerView.NO_ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutColorsBinding.inflate(inflater, parent, false)
        return ColorViewHolder(binding, onColorClick)
    }
}
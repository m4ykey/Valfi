package com.m4ykey.ui.album.adapter.viewholder

import com.m4ykey.album.ui.databinding.LayoutColorsBinding
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.ui.album.colors.ColorList
import com.m4ykey.ui.album.helpers.OnColorClick

class ColorViewHolder(
    binding : LayoutColorsBinding,
    private val onColorClick: OnColorClick
) : BaseViewHolder<ColorList, LayoutColorsBinding>(binding) {

    override fun bind(item: ColorList) {
        with(binding) {
            colorLayout.setOnClickListener { onColorClick(item) }
            viewColor.setBackgroundColor(item.color)
        }
    }
}
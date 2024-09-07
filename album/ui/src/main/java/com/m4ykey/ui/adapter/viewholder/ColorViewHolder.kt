package com.m4ykey.ui.adapter.viewholder

import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.ui.colors.ColorList
import com.m4ykey.ui.databinding.LayoutColorsBinding
import com.m4ykey.ui.helpers.OnColorClick

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
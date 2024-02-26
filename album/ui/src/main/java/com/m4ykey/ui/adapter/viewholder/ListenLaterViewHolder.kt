package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.local.model.ListenLaterEntity
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class ListenLaterViewHolder(
    private val binding : LayoutAlbumGridBinding,
    listener : OnItemClickListener<ListenLaterEntity>?
) : BaseViewHolder<ListenLaterEntity>(listener, binding.root) {

    private lateinit var currentAlbumItem : ListenLaterEntity

    companion object {
        fun create(view : ViewGroup, listener : OnItemClickListener<ListenLaterEntity>?) : ListenLaterViewHolder {
            return ListenLaterViewHolder(
                binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(view.context), view, false),
                listener = listener
            )
        }
    }

    override fun bind(item: ListenLaterEntity) {
        currentAlbumItem = item
        with(binding) {
            loadImage(imgAlbum, item.image)
            txtAlbum.text = item.name
            txtArtist.text = item.artistList
        }
    }

    override fun getItem(position: Int): ListenLaterEntity {
        return currentAlbumItem
    }
}
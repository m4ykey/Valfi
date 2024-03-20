package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class NewReleaseViewHolder(
    private val binding : LayoutAlbumGridBinding,
    listener: OnItemClickListener<AlbumItem>
) : BaseViewHolder<AlbumItem>(listener, binding.root) {

    companion object {
        fun create(view: ViewGroup, listener: OnItemClickListener<AlbumItem>) : NewReleaseViewHolder {
            return NewReleaseViewHolder(
                listener = listener,
                binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(view.context), view, false)
            )
        }
    }

    private lateinit var currentAlbumItem: AlbumItem

    override fun bind(item: AlbumItem) {
        currentAlbumItem = item
        with(binding) {
            val image = item.images.maxByOrNull { it.height * it.width }?.url
            val artistList = item.artists.joinToString(", ") { it.name }
            loadImage(imgAlbum, image.toString(), binding.root.context)
            txtAlbum.text = item.name
            txtArtist.text = artistList
        }
    }

    override fun getItem(position: Int): AlbumItem = currentAlbumItem
}
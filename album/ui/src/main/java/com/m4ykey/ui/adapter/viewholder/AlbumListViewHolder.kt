package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.databinding.LayoutAlbumListBinding

class AlbumListViewHolder(
    private val binding : LayoutAlbumListBinding,
    listener : OnItemClickListener<AlbumEntity>
) : BaseViewHolder<AlbumEntity>(listener, binding.root) {

    companion object {
        fun create(view: ViewGroup, listener: OnItemClickListener<AlbumEntity>) : AlbumListViewHolder {
            return AlbumListViewHolder(
                binding = LayoutAlbumListBinding.inflate(LayoutInflater.from(view.context), view, false),
                listener = listener
            )
        }
    }

    private lateinit var currentAlbum : AlbumEntity

    override fun bind(item: AlbumEntity) {
        currentAlbum = item
        with(binding) {
            with(item) {
                txtArtist.text = artists
                txtAlbum.text = name
                loadImage(imgAlbum, images, binding.root.context)
            }
        }
    }

    override fun getItem(position: Int): AlbumEntity = currentAlbum
}
package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding

class AlbumGridViewHolder(
    private val binding : LayoutAlbumGridBinding,
    listener : OnItemClickListener<AlbumEntity>?
) : BaseViewHolder<AlbumEntity>(listener, binding.root) {

    private lateinit var currentAlbumItem : AlbumEntity

    companion object {
        fun create(view : ViewGroup, listener : OnItemClickListener<AlbumEntity>?) : AlbumGridViewHolder {
            return AlbumGridViewHolder(
                binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(view.context), view, false),
                listener = listener
            )
        }
    }

    override fun bind(item: AlbumEntity) {
        currentAlbumItem = item
        with(binding) {
            imgAlbum.load(item.image)
            txtAlbum.text = item.name
            txtArtist.text = item.artistList
        }
    }

    override fun getItem(position: Int): AlbumEntity {
        return currentAlbumItem
    }
}
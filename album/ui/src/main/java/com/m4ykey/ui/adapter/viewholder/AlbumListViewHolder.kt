package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.databinding.LayoutAlbumListBinding

class AlbumListViewHolder(
    private val binding : LayoutAlbumListBinding,
    private val listener : OnItemClickListener<AlbumEntity>?
) : BaseViewHolder<AlbumEntity>(listener, binding.root) {

    private lateinit var currentAlbumItem : AlbumEntity

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

    companion object {
        fun create(view : ViewGroup, listener : OnItemClickListener<AlbumEntity>?) : AlbumListViewHolder {
            return AlbumListViewHolder(
                listener = listener,
                binding = LayoutAlbumListBinding.inflate(LayoutInflater.from(view.context), view, false)
            )
        }
    }

}
package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.m4ykey.core.views.animations.applyAnimation
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.callback.AlbumCallback
import com.m4ykey.ui.adapter.viewholder.NewReleaseViewHolder
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.ui.helpers.OnAlbumClick

class NewReleaseAdapter(
    private val onAlbumClick : OnAlbumClick
) : BaseRecyclerView<AlbumItem, NewReleaseViewHolder>() {

    private var lastVisibleItemPosition = -1

    override val asyncListDiffer = AsyncListDiffer(this, AlbumCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewReleaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumGridBinding.inflate(inflater, parent, false)
        return NewReleaseViewHolder(binding, onAlbumClick)
    }

    override fun onItemBindViewHolder(holder: NewReleaseViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        item?.let {
            holder.bind(it)
            holder.applyAnimation(position, lastVisibleItemPosition)
        }
    }
}
package com.m4ykey.ui.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.album.ui.databinding.LayoutTracksBinding
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.ui.album.adapter.viewholder.TrackEntityViewHolder
import com.m4ykey.ui.album.adapter.callback.TrackEntityCallback

class TrackEntityAdapter : BaseRecyclerView<TrackEntity, TrackEntityViewHolder>(TrackEntityCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onItemBindViewHolder(
        holder: TrackEntityViewHolder,
        item: TrackEntity,
        position: Int
    ) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        return differ.currentList.getOrNull(position)?.longId ?: RecyclerView.NO_ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackEntityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTracksBinding.inflate(inflater, parent, false)
        return TrackEntityViewHolder(binding)
    }
}
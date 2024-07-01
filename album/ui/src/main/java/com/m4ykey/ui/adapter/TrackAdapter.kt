package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.adapter.callback.TrackCallback
import com.m4ykey.ui.adapter.viewholder.TrackListViewHolder
import com.m4ykey.ui.databinding.LayoutTracksBinding
import com.m4ykey.ui.helpers.OnTrackClick

class TrackAdapter(
    private val onTrackClick: OnTrackClick
) : BaseRecyclerView<TrackItem, TrackListViewHolder>(TrackCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTracksBinding.inflate(inflater, parent, false)
        return TrackListViewHolder(binding, onTrackClick)
    }

    override fun onItemBindViewHolder(holder: TrackListViewHolder, item : TrackItem, position: Int) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        val item = getItem(position)
        return item?.id?.toLong() ?: position.toLong()
    }
}
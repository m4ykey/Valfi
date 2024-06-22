package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.adapter.callback.TrackCallback
import com.m4ykey.ui.adapter.viewholder.TrackListViewHolder
import com.m4ykey.ui.databinding.LayoutTracksBinding
import com.m4ykey.ui.helpers.OnTrackClick

class TrackAdapter(
    private val onTrackClick: OnTrackClick
) : BaseRecyclerView<TrackItem, TrackListViewHolder>() {

    override val asyncListDiffer = AsyncListDiffer(this, TrackCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTracksBinding.inflate(inflater, parent, false)
        return TrackListViewHolder(binding, onTrackClick)
    }

    override fun onItemBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.bind(item)
    }
}
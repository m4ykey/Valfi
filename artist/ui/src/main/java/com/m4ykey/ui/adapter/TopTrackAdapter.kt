package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.artist.ui.databinding.LayoutTopTracksBinding
import com.m4ykey.core.views.recyclerview.BaseRecyclerView
import com.m4ykey.data.domain.model.top_tracks.Track
import com.m4ykey.ui.adapter.callback.TopTrackCallback
import com.m4ykey.ui.adapter.viewholder.TopTrackViewHolder

class TopTrackAdapter : BaseRecyclerView<Track, TopTrackViewHolder>(TopTrackCallback()) {

    override fun onItemBindViewHolder(holder: TopTrackViewHolder, item: Track, position: Int) {
        holder.bind(item)
    }

    override fun getItemForPosition(position: Int): Long {
        val item = getItem(position)
        return item?.name?.toLong() ?: position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopTrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTopTracksBinding.inflate(inflater, parent, false)
        return TopTrackViewHolder(binding)
    }
}
package com.m4ykey.ui.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.top_tracks.Track

class TopTrackCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}
package com.m4ykey.ui.album.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.local.model.TrackEntity

class TrackEntityCallback : DiffUtil.ItemCallback<TrackEntity>() {
    override fun areItemsTheSame(oldItem: TrackEntity, newItem: TrackEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrackEntity, newItem: TrackEntity): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.artists == newItem.artists &&
                oldItem.durationMs == newItem.durationMs &&
                oldItem.explicit == newItem.explicit &&
                oldItem.name == newItem.name
    }
}
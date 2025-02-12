package com.m4ykey.ui.album.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.track.TrackItem

class TrackCallback : DiffUtil.ItemCallback<TrackItem>() {

    override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.artists.toTypedArray().contentEquals(newItem.artists.toTypedArray()) &&
                oldItem.durationMs == newItem.durationMs &&
                oldItem.explicit == newItem.explicit &&
                oldItem.externalUrls == newItem.externalUrls &&
                oldItem.name == newItem.name &&
                oldItem.discNumber == newItem.discNumber
    }
}
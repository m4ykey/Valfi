package com.m4ykey.ui.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.track.TrackItem

class TrackCallback : DiffUtil.ItemCallback<TrackItem>() {

    override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean =
        oldItem.id == newItem.id &&
                oldItem.name == newItem.name &&
                oldItem.explicit == newItem.explicit &&
                oldItem.discNumber == newItem.discNumber &&
                oldItem.durationMs == newItem.durationMs
}
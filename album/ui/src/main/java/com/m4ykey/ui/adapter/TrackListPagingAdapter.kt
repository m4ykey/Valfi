package com.m4ykey.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.adapter.decoration.DecoratedTrackItem
import com.m4ykey.ui.adapter.viewholder.TrackListViewHolder

class TrackListPagingAdapter(private val onTrackClick: (TrackItem) -> Unit) :
    PagingDataAdapter<DecoratedTrackItem, TrackListViewHolder>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<DecoratedTrackItem>() {
            override fun areItemsTheSame(
                oldItem: DecoratedTrackItem,
                newItem: DecoratedTrackItem
            ): Boolean = oldItem.trackItem.id == newItem.trackItem.id

            override fun areContentsTheSame(
                oldItem: DecoratedTrackItem,
                newItem: DecoratedTrackItem
            ): Boolean = oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder.create(parent, onTrackClick)
    }
}
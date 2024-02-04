package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.show
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.adapter.navigation.OnTrackClick
import com.m4ykey.ui.databinding.LayoutTracksBinding

class TrackListViewHolder(
    private val binding : LayoutTracksBinding,
    listener : OnItemClickListener<TrackItem>?
) : BaseViewHolder<TrackItem>(listener, binding.root) {

    private lateinit var currentTrackItem : TrackItem

    override fun bind(item: TrackItem) {
        currentTrackItem = item
        with(binding) {
            val artistList = item.artists.joinToString(", ") { it.name }
            val seconds = item.durationMs / 1000
            val trackDuration = String.format("%d:%02d", seconds / 60, seconds % 60)

            txtArtist.text = artistList
            txtTrackName.text = item.name
            txtDuration.text = trackDuration
            if (item.explicit) txtExplicit.show() else txtExplicit.hide()
        }
    }

    override fun getItem(position: Int): TrackItem {
        return currentTrackItem
    }

    companion object {
        fun create(parent: ViewGroup, listener: OnItemClickListener<TrackItem>?): TrackListViewHolder {
            return TrackListViewHolder(LayoutTracksBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
        }
    }
}
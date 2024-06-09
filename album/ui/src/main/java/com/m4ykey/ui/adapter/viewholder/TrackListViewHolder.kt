package com.m4ykey.ui.adapter.viewholder

import com.m4ykey.core.views.hide
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.show
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.databinding.LayoutTracksBinding
import com.m4ykey.ui.helpers.formatDuration

class TrackListViewHolder(
    binding : LayoutTracksBinding,
    private val onTrackClick : (TrackItem) -> Unit
) : BaseViewHolder<TrackItem, LayoutTracksBinding>(binding) {

    override fun bind(item: TrackItem) {
        with(binding) {
            linearLayoutTracks.setOnClickListener { onTrackClick(item) }

            val artistList = item.artists.joinToString(", ") { it.name }
            val trackDuration = formatDuration(item.durationMs / 1000)

            txtArtist.apply {
                text = artistList
                isSelected = true
            }
            txtTrackName.text = item.name
            txtDuration.text = trackDuration
            if (item.explicit) txtExplicit.show() else txtExplicit.hide()
        }
    }
}
package com.m4ykey.ui.album.adapter.viewholder

import androidx.core.view.isVisible
import com.m4ykey.album.ui.databinding.LayoutTracksBinding
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.utils.formatDuration
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.album.helpers.OnTrackClick
import com.m4ykey.ui.album.helpers.getArtistList

class TrackListViewHolder(
    binding : LayoutTracksBinding,
    private val onTrackClick : OnTrackClick
) : BaseViewHolder<TrackItem, LayoutTracksBinding>(binding) {

    override fun bind(item: TrackItem) {
        with(binding) {
            linearLayoutTracks.setOnClickListener { onTrackClick(item) }

            val trackDuration = formatDuration(item.durationMs / 1000)

            txtArtist.apply {
                text = item.getArtistList()
                isSelected = true
            }
            txtTrackName.text = item.name
            txtDuration.text = trackDuration
            txtExplicit.isVisible = item.explicit
        }
    }
}
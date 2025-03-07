package com.m4ykey.ui.album.adapter.viewholder

import androidx.core.view.isVisible
import com.m4ykey.album.ui.databinding.LayoutTracksBinding
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.utils.formatDuration
import com.m4ykey.data.local.model.TrackEntity

class TrackEntityViewHolder(
    binding : LayoutTracksBinding
) : BaseViewHolder<TrackEntity, LayoutTracksBinding>(binding) {

    override fun bind(item: TrackEntity) {
        with(binding) {
            val trackDuration = formatDuration(item.durationMs / 1000)

            txtArtist.apply {
                text = item.artists
                isSelected = true
            }

            txtTrackName.text = item.name
            txtDuration.text = trackDuration
            txtExplicit.isVisible = item.explicit
        }
    }
}
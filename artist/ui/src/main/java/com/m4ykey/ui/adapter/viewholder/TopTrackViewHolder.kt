package com.m4ykey.ui.adapter.viewholder

import androidx.core.view.isVisible
import com.m4ykey.artist.ui.databinding.LayoutTopTracksBinding
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.utils.formatDuration
import com.m4ykey.data.domain.model.top_tracks.Track
import com.m4ykey.ui.helpers.getLargestImageUrl

class TopTrackViewHolder(
    binding: LayoutTopTracksBinding
) : BaseViewHolder<Track, LayoutTopTracksBinding>(binding) {

    override fun bind(item: Track) {
        with(binding) {
            loadImage(imgAlbum, item.album.getLargestImageUrl().toString(), imgAlbum.context)
            txtTrackName.text = item.name
            val trackDuration = formatDuration(item.durationMs / 1000)
            txtDuration.text = trackDuration
            txtExplicit.isVisible = item.explicit
        }
    }
}
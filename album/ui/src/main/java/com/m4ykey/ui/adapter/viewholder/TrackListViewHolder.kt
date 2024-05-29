package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.show
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.R
import com.m4ykey.ui.adapter.decoration.DecoratedTrackItem
import com.m4ykey.ui.databinding.LayoutTracksBinding
import com.m4ykey.ui.helpers.formatDuration

class TrackListViewHolder(
    private val binding : LayoutTracksBinding,
    private val onTrackClick : (TrackItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DecoratedTrackItem) {
        with(binding) {
            linearLayoutTracks.setOnClickListener { onTrackClick(item.trackItem) }

            val artistList = item.trackItem.artists.joinToString(", ") { it.name }
            val trackDuration = formatDuration(item.trackItem.durationMs / 1000)

            txtArtist.apply {
                text = artistList
                isSelected = true
            }
            txtTrackName.text = item.trackItem.name
            txtDuration.text = trackDuration
            if (item.trackItem.explicit) txtExplicit.show() else txtExplicit.hide()

            if (item.showDiscHeader) {
                txtDiscHeader.text = txtDiscHeader.context.getString(R.string.disc, item.trackItem.discNumber)
                txtDiscHeader.show()
            } else {
                txtDiscHeader.hide()
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTrackClick: (TrackItem) -> Unit
        ): TrackListViewHolder {
            return TrackListViewHolder(
                binding = LayoutTracksBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTrackClick = onTrackClick
            )
        }
    }
}
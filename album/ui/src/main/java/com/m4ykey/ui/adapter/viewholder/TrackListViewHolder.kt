package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.databinding.LayoutTracksBinding

class TrackListViewHolder(private val binding : LayoutTracksBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view : ViewGroup) : TrackListViewHolder {
            return TrackListViewHolder(LayoutTracksBinding.inflate(LayoutInflater.from(view.context), view, false))
        }
    }

    fun bind(track : TrackItem) {
        with(binding) {
            val artistList = track.artists.joinToString(", ") { it.name }
            val seconds = track.durationMs / 1000
            val trackDuration = String.format("%d:%02d", seconds / 60, seconds % 60)

            txtTrackName.text = track.name
            txtArtist.text = artistList
            txtDuration.text = trackDuration
        }
    }

}
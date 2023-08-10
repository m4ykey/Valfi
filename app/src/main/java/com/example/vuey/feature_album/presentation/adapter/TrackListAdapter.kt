package com.example.vuey.feature_album.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vuey.core.common.utils.DiffUtils
import com.example.vuey.databinding.LayoutAlbumTrackListBinding
import com.example.vuey.feature_album.data.remote.model.spotify.album.Tracks

class TrackListAdapter : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    private var tracks = listOf<Tracks.AlbumItem>()

    fun submitTrack(newData: List<Tracks.AlbumItem>) {
        val oldData = tracks.toList()
        tracks = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class TrackViewHolder(private val binding: LayoutAlbumTrackListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trackResult: Tracks.AlbumItem) {
            with(binding) {

                txtArtist.text = trackResult.artistList.joinToString(separator = ", ") { it.artistName }
                txtTrackName.text = trackResult.trackName

                val seconds = trackResult.durationMs / 1000
                val trackDuration = String.format("%d:%02d", seconds / 60, seconds % 60)
                txtDuration.text = trackDuration
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = LayoutAlbumTrackListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}
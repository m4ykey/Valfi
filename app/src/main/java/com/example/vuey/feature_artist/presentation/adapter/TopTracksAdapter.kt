package com.example.vuey.feature_artist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.core.common.utils.DiffUtils
import com.example.vuey.databinding.LayoutTopTracksBinding
import com.example.vuey.feature_artist.data.remote.model.spotify.top_tracks.Track

class TopTracksAdapter : RecyclerView.Adapter<TopTracksAdapter.TopTracksViewHolder>() {

    private var topTracks = listOf<Track>()

    fun submitTopTracks(newData : List<Track>) {
        val oldData = topTracks.toList()
        topTracks = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopTracksViewHolder {
        val binding = LayoutTopTracksBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TopTracksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopTracksViewHolder, position: Int) {
        holder.bind(topTracks[position])
    }

    override fun getItemCount(): Int {
        return topTracks.size
    }

    inner class TopTracksViewHolder(private val binding : LayoutTopTracksBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track : Track) {
            with(binding) {
                val image = track.album.images.find { it.height == 640 && it.width == 640 }
                imgAlbum.load(image?.url) {
                    crossfade(true)
                    crossfade(500)
                    error(R.drawable.album_error)
                }
                txtArtist.text = track.artists.joinToString(separator = ", ") { it.artistName }
                txtAlbum.text = track.name
            }
        }
    }

}
package com.example.vuey.feature_music_player.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.databinding.LayoutYoutubeVideoBinding
import com.example.vuey.feature_music_player.data.remote.model.name.MusicItem
import com.example.vuey.util.utils.DiffUtils

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var video = listOf<MusicItem>()

    fun submitVideo(newData : List<MusicItem>) {
        val oldData = video.toList()
        video = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(private val binding : LayoutYoutubeVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(video : MusicItem) {
            with(binding) {
                imgVideo.load(video.snippet.thumbnails.high.url)
                txtTitle.text = video.snippet.title
                txtChannelName.text = video.snippet.channelTitle
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoAdapter.VideoViewHolder {
        val binding = LayoutYoutubeVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoAdapter.VideoViewHolder, position: Int) {
        holder.bind(video[position])
    }

    override fun getItemCount(): Int {
        return video.size
    }
}
package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.show
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.ui.databinding.LayoutTracksBinding
import com.m4ykey.ui.helpers.formatDuration

class TrackEntityViewHolder(
    private val binding : LayoutTracksBinding,
    listener : OnItemClickListener<TrackEntity>?
) : BaseViewHolder<TrackEntity>(listener, binding.root) {

    private lateinit var currentTrackItem : TrackEntity

    override fun bind(item: TrackEntity) {
        currentTrackItem = item
        with(binding) {
            val artistList = item.artistList
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

    override fun getItem(position: Int): TrackEntity {
        return currentTrackItem
    }

    companion object {
        fun create(parent: ViewGroup, listener: OnItemClickListener<TrackEntity>?): TrackEntityViewHolder {
            return TrackEntityViewHolder(LayoutTracksBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
        }
    }
}
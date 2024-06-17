package com.m4ykey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.ui.adapter.viewholder.TrackListViewHolder
import com.m4ykey.ui.databinding.LayoutTracksBinding
import com.m4ykey.ui.helpers.OnTrackClick

class TrackAdapter(
    private val onTrackClick: OnTrackClick
) : RecyclerView.Adapter<TrackListViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, COMPARATOR)

    fun submitList(list : List<TrackItem>) {
        asyncListDiffer.submitList(list)
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<TrackItem>() {
            override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTracksBinding.inflate(inflater, parent, false)
        return TrackListViewHolder(binding, onTrackClick)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size
}
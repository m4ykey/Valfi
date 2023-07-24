package com.example.vuey.feature_artist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vuey.databinding.LayoutArtistGenreBinding
import com.example.vuey.core.common.utils.DiffUtils

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var genre = listOf<String>()

    fun submitGenre(newData: List<String>) {
        val oldData = genre.toList()
        genre = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    class GenreViewHolder(private val binding: LayoutArtistGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: String) {
            binding.txtGenre.text = genre
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenreViewHolder {
        val binding = LayoutArtistGenreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genre[position])
    }

    override fun getItemCount(): Int {
        return genre.size
    }
}
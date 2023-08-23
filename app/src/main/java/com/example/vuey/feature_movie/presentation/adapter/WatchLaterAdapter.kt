package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.core.common.Constants
import com.example.vuey.core.common.utils.DiffUtils
import com.example.vuey.core.common.utils.toMovieEntity
import com.example.vuey.databinding.LayoutWatchLaterBinding
import com.example.vuey.feature_movie.data.local.entity.WatchLaterEntity
import com.example.vuey.feature_movie.presentation.MovieWatchLaterFragmentDirections

class WatchLaterAdapter : RecyclerView.Adapter<WatchLaterAdapter.WatchLaterViewHolder>() {

    private var movies = listOf<WatchLaterEntity>()

    fun submitMovie(newData : List<WatchLaterEntity>) {
        val oldData = movies.toList()
        movies = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class WatchLaterViewHolder(private val binding : LayoutWatchLaterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie : WatchLaterEntity) {
            with(binding) {
                imgMovie.load(Constants.TMDB_IMAGE_ORIGINAL + movie.moviePosterPath) {
                    crossfade(true)
                    crossfade(500)
                }
                txtMovieTitle.text = movie.movieTitle
                layoutMovie.setOnClickListener {
                    val action = MovieWatchLaterFragmentDirections.actionMovieWatchLaterFragmentToDetailMovieFragment(
                        movieEntity = movie.toMovieEntity(),
                        movieId = movie.movieId,
                        watchLaterEntity = movie,
                        movieOverview = ""
                    )
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchLaterAdapter.WatchLaterViewHolder {
        val binding = LayoutWatchLaterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return WatchLaterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchLaterAdapter.WatchLaterViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
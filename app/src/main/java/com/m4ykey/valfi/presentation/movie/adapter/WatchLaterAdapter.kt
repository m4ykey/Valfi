package com.m4ykey.valfi.presentation.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.common.Constants
import com.m4ykey.common.utils.toMovie
import com.m4ykey.common.utils.toMovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity
import com.m4ykey.valfi.databinding.LayoutWatchLaterBinding
import com.m4ykey.valfi.presentation.movie.MovieWatchLaterFragmentDirections

class WatchLaterAdapter : RecyclerView.Adapter<WatchLaterAdapter.WatchLaterViewHolder>() {

    private var movies = listOf<WatchLaterEntity>()

    fun submitMovie(newData : List<WatchLaterEntity>) {
        val oldData = movies.toList()
        movies = newData
        DiffUtil.calculateDiff(com.m4ykey.common.utils.DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
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
                        watchLaterEntity = movie,
                        movie = movie.toMovie()
                    )
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchLaterViewHolder {
        val binding = LayoutWatchLaterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return WatchLaterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchLaterViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
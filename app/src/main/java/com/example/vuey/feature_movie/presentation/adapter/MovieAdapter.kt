package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.core.common.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.core.common.utils.DateUtils
import com.example.vuey.core.common.utils.DiffUtils
import com.example.vuey.core.common.utils.formatVoteAverage
import com.example.vuey.core.common.utils.toMovie
import com.example.vuey.core.common.utils.toWatchLaterEntity
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import com.example.vuey.feature_movie.presentation.MovieFragmentDirections

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = emptyList<MovieEntity>()

    fun submitMovie(newData : List<MovieEntity>) {
        val oldData = movies.toList()
        movies = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding : LayoutMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie : MovieEntity) {
            with(binding) {
                txtMovieTitle.text = movie.movieTitle
                txtMovieOverview.text = movie.movieOverview
                imgMovie.load(TMDB_IMAGE_ORIGINAL + movie.moviePosterPath) {
                    crossfade(true)
                    crossfade(1000)
                }
                txtAverageVote.text = movie.movieVoteAverage.formatVoteAverage()
                if (movie.movieReleaseDate.isEmpty()) {
                    txtReleaseDate.visibility = View.GONE
                } else {
                    txtReleaseDate.text = DateUtils.formatAirDate(movie.movieReleaseDate)
                }
                layoutMovie.setOnClickListener {
                    val action = MovieFragmentDirections.actionMovieFragmentToDetailMovieFragment(
                        movie = movie.toMovie(),
                        movieEntity = movie,
                        isFromMovieWatchLaterFragment = false,
                        movieId = movie.movieId,
                        watchLaterEntity = movie.toWatchLaterEntity()
                    )
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder {
        val binding = LayoutMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
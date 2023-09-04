package com.example.vuey.presentation.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.presentation.movie.MovieFragmentDirections
import com.example.vuey.presentation.movie.SearchMovieFragmentDirections
import com.m4ykey.common.Constants.TMDB_IMAGE_ORIGINAL
import com.m4ykey.common.utils.DiffUtils
import com.m4ykey.common.utils.formatAirDate
import com.m4ykey.common.utils.formatVoteAverage
import com.m4ykey.common.utils.toMovie
import com.m4ykey.common.utils.toMovieEntity
import com.m4ykey.common.utils.toWatchLaterEntity
import com.m4ykey.local.movie.entity.MovieEntity
import com.m4ykey.remote.movie.model.MovieList

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = emptyList<Any>()

    fun submitMovie(newData : List<Any>) {
        val oldData = movies.toList()
        movies = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding : LayoutMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie : Any) {
            with(binding) {
                when (movie) {
                    is MovieEntity -> {
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
                            txtReleaseDate.text = formatAirDate(movie.movieReleaseDate)
                        }
                        layoutMovie.setOnClickListener {
                            val action = MovieFragmentDirections.actionMovieFragmentToDetailMovieFragment(
                                movieEntity = movie,
                                movie = movie.toMovie(),
                                watchLaterEntity = movie.toWatchLaterEntity()
                            )
                            it.findNavController().navigate(action)
                        }
                    }
                    is MovieList -> {
                        txtMovieTitle.text = movie.title
                        txtMovieOverview.text = movie.overview
                        imgMovie.load(TMDB_IMAGE_ORIGINAL + movie.posterPath) {
                            crossfade(true)
                            crossfade(1000)
                        }
                        txtAverageVote.text = movie.voteAverage.formatVoteAverage()
                        if (movie.releaseDate.isEmpty()) {
                            txtReleaseDate.visibility = View.GONE
                        } else {
                            txtReleaseDate.text = formatAirDate(movie.releaseDate)
                        }
                        layoutMovie.setOnClickListener {
                            val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToDetailMovieFragment(
                                movieEntity = movie.toMovieEntity(),
                                watchLaterEntity = movie.toWatchLaterEntity(),
                                movie = movie
                            )
                            it.findNavController().navigate(action)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding = LayoutMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
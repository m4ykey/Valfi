package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.core.common.Constants
import com.example.vuey.core.common.utils.DateUtils
import com.example.vuey.core.common.utils.formatVoteAverage
import com.example.vuey.core.common.utils.toMovieEntity
import com.example.vuey.core.common.utils.toWatchLaterEntity
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.feature_movie.presentation.SearchMovieFragmentDirections

class MoviePagingAdapter : PagingDataAdapter<MovieList, MoviePagingAdapter.MovieViewHolder>(MovieDiffUtil) {

    override fun onBindViewHolder(holder: MoviePagingAdapter.MovieViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviePagingAdapter.MovieViewHolder {
        val binding = LayoutMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    inner class MovieViewHolder(private val binding : LayoutMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie : MovieList) {
            with(binding) {
                txtMovieTitle.text = movie.title
                txtMovieOverview.text = movie.overview
                imgMovie.load(Constants.TMDB_IMAGE_ORIGINAL + movie.posterPath) {
                    crossfade(true)
                    crossfade(1000)
                }
                txtAverageVote.text = movie.voteAverage.formatVoteAverage()
                if (movie.releaseDate.isEmpty()) {
                    txtReleaseDate.visibility = View.GONE
                } else {
                    txtReleaseDate.text = DateUtils.formatAirDate(movie.releaseDate)
                }
                layoutMovie.setOnClickListener {
                    val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToDetailMovieFragment(
                        movie = movie,
                        movieEntity = movie.toMovieEntity(),
                        isFromMovieWatchLaterFragment = false,
                        movieId = movie.id,
                        watchLaterEntity = movie.toWatchLaterEntity()
                    )
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    object MovieDiffUtil : DiffUtil.ItemCallback<MovieList>() {
        override fun areItemsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
            return oldItem == newItem
        }
    }

    suspend fun submitMovie(pagingData: PagingData<MovieList>) {
        submitData(pagingData)
    }

}
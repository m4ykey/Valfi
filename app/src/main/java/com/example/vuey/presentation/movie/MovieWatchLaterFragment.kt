package com.example.vuey.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentMovieWatchLaterBinding
import com.example.vuey.presentation.movie.adapter.WatchLaterAdapter
import com.example.vuey.presentation.movie.viewmodel.MovieViewModel
import com.m4ykey.common.utils.toMovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieWatchLaterFragment : Fragment() {

    private var _binding : FragmentMovieWatchLaterBinding? = null
    private val binding get() = _binding!!

    private val watchLaterAdapter by lazy { WatchLaterAdapter() }

    private val viewModel : MovieViewModel by viewModels()

    private lateinit var currentMovie : List<WatchLaterEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieWatchLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerViewMovie.apply {
                adapter = watchLaterAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }
            lifecycleScope.launch {
                viewModel.allWatchLaterMovies.collect { movies ->
                    if (movies.isEmpty()) {
                        recyclerViewMovie.visibility = View.GONE
                        layoutEmptyList.root.visibility = View.VISIBLE
                    }
                    watchLaterAdapter.submitMovie(movies)
                    currentMovie = movies
                }
            }
            btnRandomMovie.setOnClickListener {
                if (::currentMovie.isInitialized && currentMovie.isNotEmpty()) {
                    val randomMovie = currentMovie.random()
                    val action = MovieWatchLaterFragmentDirections.actionMovieWatchLaterFragmentToDetailMovieFragment(
                        movieEntity = currentMovie[0].toMovieEntity(),
                        movieId = randomMovie.movieId,
                        watchLaterEntity = currentMovie[0],
                        movieOverview = ""
                    )
                    it.findNavController().navigate(action)
                } else {
                    com.m4ykey.common.utils.showSnackbar(
                        requireView(),
                        getString(R.string.first_you_need_add_something_to_list)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.vuey.presentation.movie

import android.annotation.SuppressLint
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
import com.example.vuey.presentation.components.EmptyLaterListScreen
import com.example.vuey.presentation.movie.adapter.WatchLaterAdapter
import com.example.vuey.presentation.movie.viewmodel.MovieViewModel
import com.m4ykey.common.utils.hideBottomNavigation
import com.m4ykey.common.utils.showSnackbar
import com.m4ykey.common.utils.toMovie
import com.m4ykey.common.utils.toMovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleScope.launch {
                val watchLaterMovies = viewModel.getWatchLaterMovieCount().firstOrNull() ?: 0
                txtCount.text = "${getString(R.string.movies_to_watch)}: $watchLaterMovies"
            }
            hideBottomNavigation(R.id.bottomNavigation)
            recyclerViewMovie.apply {
                adapter = watchLaterAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }
            lifecycleScope.launch {
                viewModel.allWatchLaterMovies.collect { movies ->
                    if (movies.isEmpty()) {
                        recyclerViewMovie.visibility = View.GONE
                        with(composeView) {
                            visibility = View.VISIBLE
                            setContent { EmptyLaterListScreen() }
                        }
                    }
                    watchLaterAdapter.submitMovie(movies)
                    currentMovie = movies
                }
            }
            btnRandomMovie.setOnClickListener {
                if (::currentMovie.isInitialized && currentMovie.isNotEmpty()) {
                    val randomMovie = currentMovie.random()
                    val action = MovieWatchLaterFragmentDirections.actionMovieWatchLaterFragmentToDetailMovieFragment(
                        movieEntity = randomMovie.toMovieEntity(),
                        watchLaterEntity = randomMovie,
                        movie = randomMovie.toMovie()
                    )
                    it.findNavController().navigate(action)
                } else {
                    showSnackbar(
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
package com.example.vuey.presentation.movie

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentMovieBinding
import com.example.vuey.presentation.movie.adapter.MovieAdapter
import com.example.vuey.presentation.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding : FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val movieAdapter by lazy { MovieAdapter() }

    private val movieViewModel : MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()
        with(binding) {
            movieRecyclerView.adapter = movieAdapter
            lifecycleScope.launch {
                coroutineScope {
                    movieViewModel.allMovies.collect { movies ->
                        if (movies.isEmpty()) {
                            movieRecyclerView.visibility = View.GONE
                            layoutEmptyList.root.visibility = View.VISIBLE
                        }
                        movieAdapter.submitMovie(movies)
                    }
                }
            }
        }
    }

    private fun setupNavigation() {
        with(binding) {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.imgLater -> {
                        findNavController().navigate(R.id.action_movieFragment_to_movieWatchLaterFragment)
                        true
                    }
                    R.id.imgAdd -> {
                        findNavController().navigate(R.id.action_movieFragment_to_searchMovieFragment)
                        true
                    }
                    R.id.imgStatistics -> {
                        findNavController().navigate(R.id.action_movieFragment_to_movieStatisticsFragment)
                        true
                    }
                    else -> { false }
                }
            }
            val laterItem = toolbar.menu.findItem(R.id.imgLater)
            val addItem = toolbar.menu.findItem(R.id.imgAdd)
            val statisticsItem = toolbar.menu.findItem(R.id.imgStatistics)
            laterItem.icon.let {
                MenuItemCompat.setIconTintList(laterItem, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.menuIconTint)))
            }
            statisticsItem.icon.let {
                MenuItemCompat.setIconTintList(statisticsItem, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.menuIconTint)))
            }
            addItem.icon.let {
                MenuItemCompat.setIconTintList(addItem, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.menuIconTint)))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
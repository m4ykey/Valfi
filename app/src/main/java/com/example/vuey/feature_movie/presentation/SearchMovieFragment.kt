package com.example.vuey.feature_movie.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.core.common.utils.SearchUtil
import com.example.vuey.databinding.FragmentSearchMovieBinding
import com.example.vuey.feature_movie.presentation.adapter.MovieAdapter
import com.example.vuey.feature_movie.presentation.viewmodel.MovieViewModel
import com.example.vuey.core.common.utils.showSnackbar
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.SearchMovieUiState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {

    private var _binding : FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MovieViewModel by viewModels()
    private val movieAdapter by lazy { MovieAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSearchMovie()
        hideBottomNavigation()
        searchMovie()

        with(binding) {
            imgBack.setOnClickListener { findNavController().navigateUp() }
            recyclerViewMovie.adapter = movieAdapter
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun searchMovie() {
        with(binding) {
            etSearch.addTextChangedListener {
                val searchHandler = Handler()
                searchHandler.removeCallbacksAndMessages(null)

                val searchMovie = etSearch.text.toString()

                if (searchMovie.isNotEmpty()) {
                    progressBar.visibility = View.VISIBLE
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_clear, 0)
                    searchHandler.postDelayed({
                        lifecycleScope.launch {
                            viewModel.searchMovie(searchMovie)
                        }
                        progressBar.visibility = View.GONE
                    }, 500)
                } else {
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(0,0, 0, 0)
                    progressBar.visibility = View.GONE
                }
            }

            etSearch.setOnTouchListener { _, event ->
                val drawableEndIndex = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawableEnd = etSearch.compoundDrawables[drawableEndIndex]
                    drawableEnd?.let {
                        if (event.rawX >= (etSearch.right - it.bounds.width())) {
                            etSearch.text?.clear()
                            return@setOnTouchListener true
                        }
                    }
                }
                return@setOnTouchListener false
            }
        }
    }

    private fun observeSearchMovie() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieSearchUiState.collect { uiState ->
                    with(binding) {
                        when(uiState) {
                            is SearchMovieUiState.Success -> {
                                progressBar.visibility = View.GONE
                                val moviesWithScore = uiState.movieData.map { movie ->
                                    movie to SearchUtil.calculateMovieMatchingScore(
                                        movie,
                                        etSearch.text.toString()
                                    )
                                }

                                val sortedMovie = moviesWithScore.sortedByDescending { it.second }
                                val sortedMovieList = sortedMovie.map { it.first }

                                movieAdapter.submitMovie(sortedMovieList)
                            }
                            is SearchMovieUiState.Failure -> {
                                progressBar.visibility = View.GONE
                                showSnackbar(requireView(), uiState.message, Snackbar.LENGTH_LONG)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
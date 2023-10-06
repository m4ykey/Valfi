package com.example.vuey.presentation.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchMovieBinding
import com.example.vuey.presentation.movie.adapter.MovieAdapter
import com.example.vuey.presentation.movie.viewmodel.MovieViewModel
import com.example.vuey.presentation.movie.viewmodel.ui_state.SearchMovieUiState
import com.m4ykey.common.utils.calculateMovieMatchingScore
import com.m4ykey.common.utils.hideBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {

    private var _binding: FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels()
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

        with(binding) {
            observeSearchMovie()
            hideBottomNavigation(R.id.bottomNavigation)
            searchMovie()
            imgBack.setOnClickListener { findNavController().navigateUp() }
            recyclerViewMovie.adapter = movieAdapter
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun FragmentSearchMovieBinding.searchMovie() {
        with(etSearch) {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchMovie = etSearch.text.toString()

                    if (searchMovie.isNotEmpty()) {
                        progressBar.visibility = View.VISIBLE
                        etSearch.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_clear,
                            0
                        )

                        lifecycleScope.launch {
                            viewModel.searchMovie(searchMovie)
                        }
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.GONE
                        etSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            setOnTouchListener { _, event ->
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

    private fun FragmentSearchMovieBinding.observeSearchMovie() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieSearchUiState.collect { uiState ->
                    when (uiState) {
                        is SearchMovieUiState.Success -> {
                            progressBar.visibility = View.GONE

                            val movies = uiState.movieData.map { movie ->
                                movie to calculateMovieMatchingScore(movie, etSearch.text.toString())
                            }

                            val sortMovie = movies.sortedByDescending { it.second }
                            val sortedList = sortMovie.map { it.first }

                            movieAdapter.submitMovie(sortedList)
                        }
                        is SearchMovieUiState.Failure -> {
                            progressBar.visibility = View.GONE
                            Log.i("MovieError", "observeSearchMovie: ${uiState.message}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
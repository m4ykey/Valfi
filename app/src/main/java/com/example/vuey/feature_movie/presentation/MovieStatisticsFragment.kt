package com.example.vuey.feature_movie.presentation

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentMovieStatisticsBinding
import com.example.vuey.feature_movie.presentation.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieStatisticsFragment : Fragment() {

    private var _binding : FragmentMovieStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigation()

        with(binding) {

            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

            lifecycleScope.launch {
                coroutineScope {

                    val movieCountEndValue = viewModel.getMovieCount().firstOrNull() ?: 0
                    val totalTime = viewModel.getTotalLength().firstOrNull() ?: 0

                    val movieHour = totalTime / 60
                    val movieMinute = totalTime % 60
                    val movieRuntime = if (totalTime == 0) {
                        String.format("%d min", movieMinute)
                    } else {
                        String.format(
                            "%d ${getString(R.string.hour)} %d min",
                            movieHour,
                            movieMinute
                        )
                    }

                    val valueAnimator = ValueAnimator.ofInt(0, movieCountEndValue)
                    valueAnimator.apply {
                        duration = 2000
                        interpolator = AccelerateDecelerateInterpolator()
                    }
                    valueAnimator.addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue as Int
                        txtMovieCount.text = if (animatedValue <= movieCountEndValue) animatedValue.toString() else movieCountEndValue.toString()
                    }
                    valueAnimator.start()
                    txtLength.text = movieRuntime

                }
            }
        }

    }

    private fun hideBottomNavigation() {
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
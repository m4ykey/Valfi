package com.example.vuey.feature_album.presentation.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
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
import com.example.vuey.databinding.FragmentAlbumStatisticsBinding
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.lang.Integer.max

@AndroidEntryPoint
class AlbumStatisticsFragment : Fragment() {

    private var _binding : FragmentAlbumStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AlbumViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigation()

        with(binding) {

            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

            lifecycleScope.launch {
                coroutineScope {

                    val albumCountEndValue = viewModel.getAlbumCount().firstOrNull() ?: 0
                    val totalTracksEndValue = viewModel.getTotalTracks().firstOrNull() ?: 0
                    val totalLength = viewModel.getTotalLength().firstOrNull() ?: 0

                    val albumTimeHour = totalLength / (1000 * 60 * 60)
                    val albumTimeMinute = (totalLength / (1000 * 60)) % 60
                    val albumTimeSeconds = (totalLength / 1000) % 60

                    txtLength.text = if (albumTimeHour == 0) {
                        String.format("%d min %d ${getString(R.string.sec)}", albumTimeMinute, albumTimeSeconds)
                    } else if (albumTimeMinute == 0){
                        String.format("%d ${getString(R.string.hour)}", albumTimeHour)
                    } else {
                        String.format("%d ${getString(R.string.hour)} %d min", albumTimeHour, albumTimeMinute)
                    }

                    val valueAnimator = ValueAnimator.ofInt(0, max(albumCountEndValue, totalTracksEndValue))
                    valueAnimator.apply {
                        duration = 2000
                        interpolator = AccelerateDecelerateInterpolator()
                    }

                    valueAnimator.addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue as Int
                        txtAlbumsNumber.text = if (animatedValue <= albumCountEndValue) animatedValue.toString() else albumCountEndValue.toString()
                        txtSongs.text = if (animatedValue <= totalTracksEndValue) animatedValue.toString() else totalTracksEndValue.toString()
                    }

                    valueAnimator.start()
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
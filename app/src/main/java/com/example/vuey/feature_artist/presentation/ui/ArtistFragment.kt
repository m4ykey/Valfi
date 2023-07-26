package com.example.vuey.feature_artist.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.vuey.R
import com.example.vuey.core.common.utils.showSnackbar
import com.example.vuey.databinding.FragmentArtistBinding
import com.example.vuey.feature_artist.presentation.adapter.GenreAdapter
import com.example.vuey.feature_artist.presentation.adapter.TopTracksAdapter
import com.example.vuey.feature_artist.presentation.viewmodel.ArtistViewModel
import com.example.vuey.feature_artist.presentation.viewmodel.uistate.ArtistInfoUiState
import com.example.vuey.feature_artist.presentation.viewmodel.uistate.ArtistTopTracksUiState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class ArtistFragment : Fragment() {

    private val artistViewModel: ArtistViewModel by viewModels()

    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!

    private val genreAdapter by lazy { GenreAdapter() }
    private val topTracksAdapter by lazy { TopTracksAdapter() }

    private val args: ArtistFragmentArgs by navArgs()

    private var isFollowed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            with(artistViewModel) {
                getArtistInfo(args.artistId)
                getArtistTopTracks(args.artistId)
            }
        }

        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            recyclerViewTopTracks.apply {
                adapter = topTracksAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            recyclerViewGenre.apply {
                adapter = genreAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
            btnFollow.setOnClickListener {
                isFollowed = !isFollowed
                if (isFollowed) {
                    btnFollow.text = getString(R.string.followed)
                } else {
                    btnFollow.text = getString(R.string.follow)
                }
            }
        }

        hideBottomNavigation()
        observeArtistInfo()
        observeArtistTopTracks()
    }

    private fun observeArtistTopTracks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                artistViewModel.artistTopTracksUiState.collect { uiState ->
                    with(binding) {
                        when (uiState) {
                            is ArtistTopTracksUiState.Success -> {
                                progressBar.visibility = View.GONE
                                if (uiState.topTracksData.isEmpty()) {
                                    recyclerViewTopTracks.visibility = View.GONE
                                }
                                topTracksAdapter.submitTopTracks(uiState.topTracksData)
                            }
                            is ArtistTopTracksUiState.Loading -> { progressBar.visibility = View.VISIBLE }
                            is ArtistTopTracksUiState.Failure -> {
                                progressBar.visibility = View.GONE
                                showSnackbar(requireView(), uiState.message, Snackbar.LENGTH_LONG)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }

    private fun observeArtistInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                artistViewModel.artistInfoUiState.collect { uiState ->
                    with(binding) {
                        when (uiState) {
                            is ArtistInfoUiState.Loading -> { progressBar.visibility = View.VISIBLE }
                            is ArtistInfoUiState.Success -> {
                                progressBar.visibility = View.GONE

                                val artistInfo = uiState.artistData

                                val artistImage = artistInfo.images.find { it.height == 640 && it.width == 640 }
                                imgArtist.load(artistImage!!.url)

                                txtArtist.text = artistInfo.name
                                if (artistInfo.genres.isEmpty()) {
                                    txtEmptyGenres.visibility = View.VISIBLE
                                } else {
                                    genreAdapter.submitGenre(artistInfo.genres)
                                }
                            }
                            is ArtistInfoUiState.Failure -> {
                                progressBar.visibility = View.GONE
                                showSnackbar(requireView(), uiState.message, Snackbar.LENGTH_LONG)
                            }
                        }
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
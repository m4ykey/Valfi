package com.example.vuey.feature_album.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
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
import com.example.vuey.core.common.utils.SearchUtil.calculateAlbumMatchingScore
import com.example.vuey.core.common.utils.showSnackbar
import com.example.vuey.databinding.FragmentSearchAlbumBinding
import com.example.vuey.feature_album.data.remote.model.spotify.album.Album
import com.example.vuey.feature_album.presentation.adapter.AlbumAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.SearchAlbumUiState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class SearchAlbumFragment : Fragment() {

    private var _binding: FragmentSearchAlbumBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: AlbumViewModel by viewModels()
    private val albumAdapter by lazy { AlbumAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeSearchAlbum()
        hideBottomNavigation()
        searchAlbum()

        with(binding) {
            recyclerViewAlbum.adapter = albumAdapter
            imgBack.setOnClickListener { findNavController().navigateUp() }
        }

    }

    private fun searchAlbum() {
        with(binding) {
            etSearch.addTextChangedListener {
                val searchHandler = Handler()
                searchHandler.removeCallbacksAndMessages(null)

                val searchAlbum = etSearch.text.toString()

                if (searchAlbum.isNotEmpty()) {
                    progressBar.visibility = View.VISIBLE
                    searchHandler.postDelayed({
                        lifecycleScope.launch {
                            searchViewModel.searchAlbum(searchAlbum)
                        }
                        progressBar.visibility = View.GONE
                    }, 500)
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun observeSearchAlbum() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.albumSearchUiState.collect { uiState ->
                    with(binding) {
                        when (uiState) {
                            is SearchAlbumUiState.Success -> {
                                progressBar.visibility = View.GONE

                                val albumsWithScore = uiState.albumData.map { album ->
                                    album to calculateAlbumMatchingScore(album, etSearch.text.toString())
                                }

                                val sortedAlbum = albumsWithScore.sortedByDescending { it.second }
                                val sortedAlbumList = sortedAlbum.map { it.first }

                                albumAdapter.submitAlbums(sortedAlbumList)
                            }

                            is SearchAlbumUiState.Failure -> {
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
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.vuey.feature_album.presentation.ui

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.core.common.utils.showSnackbar
import com.example.vuey.databinding.FragmentSearchAlbumBinding
import com.example.vuey.feature_album.presentation.adapter.AlbumAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.SearchAlbumUiState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        searchAlbum()

        with(binding) {
            recyclerViewAlbum.apply {
                adapter = albumAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            imgBack.setOnClickListener { findNavController().navigateUp() }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun searchAlbum() {
        with(binding) {
            etSearch.addTextChangedListener {
                val searchHandler = Handler()
                searchHandler.removeCallbacksAndMessages(null)

                val searchAlbum = etSearch.text.toString()

                if (searchAlbum.isNotEmpty()) {
                    progressBar.visibility = View.VISIBLE
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_clear, 0)
                    searchHandler.postDelayed({
                        lifecycleScope.launch {
                            searchViewModel.searchAlbum(searchAlbum)
                        }
                        progressBar.visibility = View.GONE
                    }, 500)
                } else {
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
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

    private fun observeSearchAlbum() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.albumSearchUiState.collect { uiState ->
                    with(binding) {
                        when (uiState) {
                            is SearchAlbumUiState.Success -> {
                                albumAdapter.submitAlbums(uiState.albumData)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

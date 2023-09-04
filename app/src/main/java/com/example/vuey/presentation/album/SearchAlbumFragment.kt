package com.example.vuey.presentation.album

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchAlbumBinding
import com.example.vuey.presentation.album.adapter.AlbumAdapter
import com.example.vuey.presentation.album.viewmodel.AlbumViewModel
import com.example.vuey.presentation.album.viewmodel.ui_state.SearchAlbumUiState
import com.google.android.material.snackbar.Snackbar
import com.m4ykey.common.utils.calculateAlbumMatchingScore
import com.m4ykey.common.utils.hideBottomNavigation
import com.m4ykey.common.utils.showSnackbar
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

        with(binding) {
            observeSearchAlbum()
            hideBottomNavigation(R.id.bottomNavigation)
            searchAlbum()
            recyclerViewAlbum.apply {
                adapter = albumAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            imgBack.setOnClickListener { findNavController().navigateUp() }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun FragmentSearchAlbumBinding.searchAlbum() {
        with(etSearch) {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchAlbum = etSearch.text.toString()

                    if (searchAlbum.isNotEmpty()) {
                        progressBar.visibility = View.VISIBLE
                        etSearch.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_clear,
                            0
                        )

                        lifecycleScope.launch {
                            searchViewModel.searchAlbum(searchAlbum)
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

    private fun FragmentSearchAlbumBinding.observeSearchAlbum() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.albumSearchUiState.collect { uiState ->
                    when (uiState) {
                        is SearchAlbumUiState.Success -> {

                            val albums = uiState.albumData.map { album ->
                                album to calculateAlbumMatchingScore(album, etSearch.text.toString())
                            }

                            val sortAlbum = albums.sortedByDescending { it.second }
                            val sortedList = sortAlbum.map { it.first }

                            albumAdapter.submitAlbums(sortedList)
                        }

                        is SearchAlbumUiState.Failure -> {
                            progressBar.visibility = View.GONE
                            showSnackbar(
                                requireView(),
                                uiState.message,
                                Snackbar.LENGTH_LONG
                            )
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

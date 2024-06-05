package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.paging.handleLoadState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.adapter.LoadStateAdapter
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.createGridLayoutManager
import com.m4ykey.core.views.show
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.SearchAlbumPagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumSearchBinding
import com.m4ykey.ui.uistate.AlbumListUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumSearchFragment : BaseFragment<FragmentAlbumSearchBinding>(
    FragmentAlbumSearchBinding::inflate
) {

    private var isClearButtonVisible = false
    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var searchAdapter : SearchAlbumPagingAdapter
    private val debouncingSearch = DebouncingSearch()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        setupToolbar()
        setupRecyclerView()
        searchAlbums()

        lifecycleScope.launch {
            viewModel.albums.observe(viewLifecycleOwner) { state -> handleSearchState(state) }
        }
    }

    private fun handleSearchState(state : AlbumListUiState?) {
        state ?: return
        binding?.apply {
            progressbar.isVisible = state.isLoading
            rvSearchAlbums.isVisible = !state.isLoading
            state.error?.let { showToast(requireContext(), it) }
            state.albumList?.let { search ->
                searchAdapter.submitData(lifecycle, search)
            }
        }
    }

    private fun searchAlbums() {
        binding?.etSearch?.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        val searchQuery = text.toString().trim()
                        if (searchQuery.isNotEmpty()) {
                            debouncingSearch.submit(searchQuery)
                            binding?.rvSearchAlbums?.isEnabled = false
                        } else {
                            showToast(requireContext(), getString(R.string.empty_search))
                        }
                    }
                }
                actionId == EditorInfo.IME_ACTION_SEARCH
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.rvSearchAlbums?.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            val onAlbumClick : (AlbumItem) -> Unit = { album ->
                val action = AlbumSearchFragmentDirections.actionAlbumSearchFragmentToAlbumDetailFragment(album.id)
                findNavController().navigate(action)
            }

            searchAdapter = SearchAlbumPagingAdapter(onAlbumClick)

            val headerAdapter = LoadStateAdapter { searchAdapter.retry() }
            val footerAdapter = LoadStateAdapter { searchAdapter.retry() }

            adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )

            layoutManager = createGridLayoutManager(headerAdapter, footerAdapter)

            searchAdapter.addLoadStateListener { loadState ->
                handleLoadState(
                    loadState = loadState,
                    adapter = searchAdapter,
                    recyclerView = this,
                    progressBar = binding!!.progressbar
                )
            }
        }
    }

    private fun setupToolbar() {
        binding?.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            etSearch.doOnTextChanged { text, _, _, _ ->
                val isSearchEmpty = text.isNullOrBlank()
                handleClearButtonVisibility(isSearchEmpty)
            }

            imgClear.setOnClickListener {
                etSearch.setText("")
                hideClearButtonWithAnimation()
            }
        }
    }

    private fun handleClearButtonVisibility(isSearchEmpty : Boolean) {
        if (!isSearchEmpty && !isClearButtonVisible) {
            showClearButtonWithAnimation()
            isClearButtonVisible = true
        } else if (isSearchEmpty && isClearButtonVisible) {
            hideClearButtonWithAnimation()
            isClearButtonVisible = false
        }
    }

    private fun View.animationProperties(translationXValue : Float, alphaValue : Float, interpolator : Interpolator) {
        animate()
            .translationX(translationXValue)
            .alpha(alphaValue)
            .setInterpolator(interpolator)
            .start()
    }

    private fun showClearButtonWithAnimation() {
        binding?.imgClear?.apply {
            translationX = 100f
            alpha = 0f
            show()

            animationProperties(0f, 1f, DecelerateInterpolator())
        }
    }

    private fun hideClearButtonWithAnimation() {
        binding?.imgClear?.apply {
            animationProperties(width.toFloat(), 0f, AccelerateInterpolator())
        }
    }

    private fun resetSearchState() {
        binding?.apply {
            if (etSearch.text.isNullOrBlank()) {
                imgClear.isVisible = false
                isClearButtonVisible = false
            } else {
                imgClear.isVisible = true
                isClearButtonVisible = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resetSearchState()
    }

    private inner class DebouncingSearch {
        private var searchJob : Job? = null

        fun submit(searchQuery : String) {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500L)
                viewModel.searchAlbums(searchQuery)
                binding?.rvSearchAlbums?.isEnabled = true
            }
        }
    }
}
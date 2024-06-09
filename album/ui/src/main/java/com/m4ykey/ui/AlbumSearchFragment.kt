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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.SearchAlbumAdapter
import com.m4ykey.ui.databinding.FragmentAlbumSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumSearchFragment : BaseFragment<FragmentAlbumSearchBinding>(
    FragmentAlbumSearchBinding::inflate
) {

    private var isClearButtonVisible = false
    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var searchAdapter : SearchAlbumAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        setupToolbar()
        setupRecyclerView()
        searchAlbums()

        lifecycleScope.launch {
            viewModel.albums.observe(viewLifecycleOwner) { albums ->
                searchAdapter.submitList(albums)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding?.progressbar?.isVisible = isLoading
            }

            viewModel.isError.observe(viewLifecycleOwner) { isError ->
                if (isError) showToast(requireContext(), "Error loading data")
            }
        }
    }

    private fun searchAlbums() {
        binding?.etSearch?.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val searchQuery = binding?.etSearch?.text?.toString()
                    if (searchQuery?.isNotEmpty() == true) {
                        viewModel.resetSearch()
                        lifecycleScope.launch { viewModel.searchAlbums(searchQuery) }
                    } else {
                        showToast(requireContext(), getString(R.string.empty_search))
                    }
                }
            }
            actionId == EditorInfo.IME_ACTION_SEARCH
        }
    }

    private fun setupRecyclerView() {
        binding?.rvSearchAlbums?.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            val onAlbumClick : (AlbumItem) -> Unit = { album ->
                val action = AlbumSearchFragmentDirections.actionAlbumSearchFragmentToAlbumDetailFragment(album.id)
                findNavController().navigate(action)
            }

            searchAdapter = SearchAlbumAdapter(onAlbumClick)
            adapter = searchAdapter

            layoutManager = GridLayoutManager(requireContext(), 3)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        val searchQuery = binding?.etSearch?.text.toString()
                        if (!viewModel.isPaginationEnded && viewModel.isLoading.value == false && searchQuery.isNotEmpty()) {
                            lifecycleScope.launch { viewModel.searchAlbums(searchQuery) }
                        }
                    }
                }
            })
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
            isVisible = true

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
}
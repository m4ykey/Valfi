package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.album.ui.databinding.FragmentAlbumNewReleaseBinding
import com.m4ykey.core.Constants
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.core.views.recyclerview.setupGridLayoutManager
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.ui.adapter.NewReleaseAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumNewReleaseFragment : BaseFragment<FragmentAlbumNewReleaseBinding>(
    FragmentAlbumNewReleaseBinding::inflate
) {
    private val viewModel by viewModels<AlbumViewModel>()
    private val albumAdapter by lazy { createNewReleaseAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        handleRecyclerViewButton()

        if (viewModel.newRelease.value.isEmpty()) {
            lifecycleScope.launch {
                viewModel.getNewReleases()
            }
        }

        lifecycleScope.launch {
            viewModel.newRelease.collect { releases ->
                albumAdapter.submitList(releases)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { binding.progressBar.isVisible = it }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    showToast(requireContext(), it)
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        setupRecyclerView()

    }

    private fun createNewReleaseAdapter() : NewReleaseAdapter {
        return NewReleaseAdapter(
            onAlbumClick = { album ->
                val action = AlbumNewReleaseFragmentDirections.actionAlbumNewReleaseFragmentToAlbumDetailFragment(album.id)
                findNavController().navigate(action)
            }
        )
    }

    private fun handleRecyclerViewButton() {
        binding.apply {
            recyclerViewNewRelease.addOnScrollListener(scrollListener(btnToTop))
                btnToTop.setOnClickListener {
                recyclerViewNewRelease.smoothScrollToPosition(0)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewNewRelease.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(Constants.SPACE_BETWEEN_ITEMS)))
            adapter = albumAdapter
            val isTablet = context.resources.configuration.smallestScreenWidthDp >= 600
            layoutManager = if (isTablet) {
                setupGridLayoutManager(requireContext(), 150f)
            } else {
                setupGridLayoutManager(requireContext(), 110f)
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        if (!viewModel.isPaginationEnded && !viewModel.isLoading.value) {
                            lifecycleScope.launch { viewModel.getNewReleases() }
                        }
                    }
                }
            })
        }
    }
}
package com.m4ykey.ui.album

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.album.ui.databinding.FragmentAlbumNewReleaseBinding
import com.m4ykey.core.Constants
import com.m4ykey.core.network.UiState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.ui.album.adapter.NewReleaseAdapter
import com.m4ykey.ui.album.helpers.createGridLayoutManager
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
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

        if (viewModel.newRelease.value !is UiState.Loading) {
            lifecycleScope.launch {
                viewModel.getNewReleases()
            }
        }

        lifecycleScope.launch {
            viewModel.newRelease.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        binding.progressBar.isVisible = false
                        albumAdapter.submitList(uiState.data, isAppend = true)
                    }
                    is UiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is UiState.Error -> {
                        binding.progressBar.isVisible = false
                        uiState.exception.message?.let {
                            showToast(requireContext(), it)
                        }
                    }
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
            val currentScrollPosition = (binding.recyclerViewNewRelease.layoutManager as? GridLayoutManager)
                ?.findFirstVisibleItemPosition()

            if (itemDecorationCount == 0) {
                addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(Constants.SPACE_BETWEEN_ITEMS)))
            }
            adapter = albumAdapter
            currentScrollPosition?.let { position ->
                layoutManager?.scrollToPosition(position)
            }
            layoutManager = createGridLayoutManager(requireContext())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && !viewModel.isPaginationEnded) {
                        lifecycleScope.launch { viewModel.getNewReleases() }
                    }
                }
            })
        }
    }
}
package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.Constants
import com.m4ykey.core.paging.handleLoadState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.adapter.LoadStateAdapter
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.createGridLayoutManager
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.NewReleasePagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumNewReleaseBinding
import com.m4ykey.ui.uistate.AlbumListUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumNewReleaseFragment : BaseFragment<FragmentAlbumNewReleaseBinding>(
    FragmentAlbumNewReleaseBinding::inflate
) {
    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var albumAdapter : NewReleasePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        lifecycleScope.launch {
            delay(500L)
            viewModel.getNewReleases()
        }
        viewModel.newRelease.observe(viewLifecycleOwner) { state -> handleNewReleaseState(state) }

        binding?.toolbar?.setNavigationOnClickListener { findNavController().navigateUp() }
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        binding?.recyclerViewNewRelease?.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(Constants.SPACE_BETWEEN_ITEMS)))

            val onAlbumClick : (AlbumItem) -> Unit = { album ->
                val action = AlbumNewReleaseFragmentDirections.actionAlbumNewReleaseFragmentToAlbumDetailFragment(album.id)
                findNavController().navigate(action)
            }

            albumAdapter = NewReleasePagingAdapter(onAlbumClick)

            val headerAdapter = LoadStateAdapter { albumAdapter.retry() }
            val footerAdapter = LoadStateAdapter { albumAdapter.retry() }

            adapter = albumAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )

            layoutManager = createGridLayoutManager(headerAdapter, footerAdapter)

            albumAdapter.addLoadStateListener { loadState ->
                handleLoadState(
                    loadState = loadState,
                    recyclerView = this,
                    progressBar = binding!!.progressbar,
                    adapter = albumAdapter
                )
            }
        }
    }

    private fun handleNewReleaseState(state : AlbumListUiState?) {
        state ?: return
        binding?.apply {
            progressbar.isVisible = state.isLoading
            recyclerViewNewRelease.isVisible = !state.isLoading
            state.error?.let { showToast(requireContext(), it) }
            state.albumList?.let { items ->
                albumAdapter.submitData(lifecycle, items)
            }
        }
    }
}
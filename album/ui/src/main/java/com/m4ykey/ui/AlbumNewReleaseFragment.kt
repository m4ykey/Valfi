package com.m4ykey.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.Constants
import com.m4ykey.core.network.ErrorState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.setupGridLayoutManager
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.ui.adapter.NewReleaseAdapter
import com.m4ykey.ui.databinding.FragmentAlbumNewReleaseBinding
import com.m4ykey.ui.helpers.OnAlbumClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class AlbumNewReleaseFragment : BaseFragment<FragmentAlbumNewReleaseBinding>(
    FragmentAlbumNewReleaseBinding::inflate
) {
    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var albumAdapter : NewReleaseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        lifecycleScope.launch {
            viewModel.getNewReleases()
        }

        lifecycleScope.launch {
            viewModel.newRelease.collect { albumAdapter.submitList(it) }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { binding.progressbar.isVisible = it }
        }

        lifecycleScope.launch {
            viewModel.isError.collect { errorState ->
                when (errorState) {
                    is ErrorState.Error -> showToast(requireContext(), errorState.message.toString())
                    else -> {}
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        binding.recyclerViewNewRelease.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(Constants.SPACE_BETWEEN_ITEMS)))

            val onAlbumClick : OnAlbumClick = { album ->
                val action = AlbumNewReleaseFragmentDirections.actionAlbumNewReleaseFragmentToAlbumDetailFragment(album.id)
                findNavController().navigate(action)
            }

            albumAdapter = NewReleaseAdapter(onAlbumClick)
            adapter = albumAdapter

            layoutManager = setupGridLayoutManager(requireContext(), 110f)
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
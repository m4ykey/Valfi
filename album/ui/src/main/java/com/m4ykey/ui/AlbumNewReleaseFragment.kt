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
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.setupGridLayoutManager
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.NewReleaseAdapter
import com.m4ykey.ui.databinding.FragmentAlbumNewReleaseBinding
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

        viewModel.newRelease.observe(viewLifecycleOwner) { albums ->
            albumAdapter.submitList(albums)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding?.progressbar?.isVisible = isLoading
        }

        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) showToast(requireContext(), "Error loading data")
        }

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

            albumAdapter = NewReleaseAdapter(onAlbumClick)
            adapter = albumAdapter

                layoutManager = setupGridLayoutManager(requireContext(), 110f)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        if (!viewModel.isPaginationEnded && viewModel.isLoading.value == false) {
                            lifecycleScope.launch { viewModel.getNewReleases() }
                        }
                    }
                }
            })
        }
    }
}
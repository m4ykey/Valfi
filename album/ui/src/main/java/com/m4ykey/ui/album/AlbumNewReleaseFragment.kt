package com.m4ykey.ui.album

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.m4ykey.album.ui.databinding.FragmentAlbumNewReleaseBinding
import com.m4ykey.core.Constants
import com.m4ykey.core.observeUiState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.ui.album.adapter.NewReleaseAdapter
import com.m4ykey.ui.album.helpers.createGridLayoutManager
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
import com.m4ykey.ui.navigation.AlbumNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumNewReleaseFragment : BaseFragment<FragmentAlbumNewReleaseBinding>(
    FragmentAlbumNewReleaseBinding::inflate
) {
    private val viewModel by viewModels<AlbumViewModel>()
    private val albumAdapter by lazy { createNewReleaseAdapter() }
    @Inject
    lateinit var navigator : AlbumNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleRecyclerViewButton()
        loadData()
        observeViewModelStates()

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        setupRecyclerView()

    }

    private fun observeViewModelStates() {
        observeUiState(
            progressBar = binding.progressBar,
            lifecycleScope = lifecycleScope,
            context = requireContext(),
            flow = viewModel.newRelease,
            onSuccess = { newRelease ->
                newRelease.let {
                    lifecycleScope.launch {
                        albumAdapter.submitData(it)
                    }
                }
            }
        )
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.getNewReleases()
        }
    }

    private fun createNewReleaseAdapter() : NewReleaseAdapter {
        return NewReleaseAdapter(
            onAlbumClick = { album -> navigator.navigateToAlbumDetail(album.id) }
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
        }
    }
}
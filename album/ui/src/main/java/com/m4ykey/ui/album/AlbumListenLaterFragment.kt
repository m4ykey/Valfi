package com.m4ykey.ui.album

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m4ykey.album.ui.R
import com.m4ykey.album.ui.databinding.FragmentAlbumListenLaterBinding
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.ui.album.adapter.AlbumAdapter
import com.m4ykey.ui.album.helpers.BooleanWrapper
import com.m4ykey.ui.album.helpers.createGridLayoutManager
import com.m4ykey.ui.album.helpers.hideSearchEditText
import com.m4ykey.ui.album.helpers.setupSearch
import com.m4ykey.ui.album.helpers.showSearchEditText
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumListenLaterFragment : BaseFragment<FragmentAlbumListenLaterBinding>(
    FragmentAlbumListenLaterBinding::inflate
) {

    private val viewModel by viewModels<AlbumViewModel>()
    private val albumAdapter by lazy { createAlbumAdapter() }
    private var isSearchEditTextVisible = BooleanWrapper(false)
    private var isHidingAnimationRunning = BooleanWrapper(false)

    private var savedSearchQuery : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        setupToolbar()
        setupRecyclerView()
        getRandomAlbum()
        handleRecyclerViewButton()
        setupSearch(
            etSearch = binding.etSearch,
            viewModel = viewModel,
            lifecycleScope = lifecycleScope,
            savedSearchQuery = savedSearchQuery
        )

        binding.apply {
            viewModel.apply {
                lifecycleScope.launch { getListenLaterCount() }
                lifecycleScope.launch { getListenLaterAlbums() }
                lifecycleScope.launch { getRandomAlbum() }
                lifecycleScope.launch {
                    albumEntity.collect { albums ->
                        if (albums.isEmpty()) {
                            albumAdapter.submitList(emptyList())
                            linearLayoutEmptyList.isVisible = true
                            linearLayoutEmptySearch.isVisible = false
                        } else {
                            albumAdapter.submitList(albums)
                            binding.linearLayoutEmptyList.isVisible = false
                            binding.linearLayoutEmptySearch.isVisible = false
                        }
                    }
                }

                lifecycleScope.launch {
                    searchResult.collect { albums ->
                        if (albums.isEmpty()) {
                            albumAdapter.submitList(emptyList())
                            linearLayoutEmptySearch.isVisible = true
                            linearLayoutEmptyList.isVisible = false
                        } else {
                            albumAdapter.submitList(albums)
                            linearLayoutEmptySearch.isVisible = false
                        }
                    }
                }

                lifecycleScope.launch {
                    listenLaterCount.collect { count ->
                        txtAlbumCount.text = getString(R.string.album_count, count)
                    }
                }
            }

            imgHide.setOnClickListener {
                hideSearchEditText(
                    coroutineScope = lifecycleScope,
                    isSearchEditTextVisible = isSearchEditTextVisible,
                    translationYValue = -30f,
                    isHidingAnimationRunning = isHidingAnimationRunning,
                    linearLayout = linearLayoutSearch
                )
                etSearch.setText(getString(R.string.empty_string))
            }

            chipSearch.setOnClickListener {
                isSearchEditTextVisible = showSearchEditText(isSearchEditTextVisible, linearLayoutSearch, -30f)
            }
        }
    }

    private fun createAlbumAdapter() : AlbumAdapter {
        return AlbumAdapter(
            onAlbumClick = { album ->
                val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(album.id)
                findNavController().navigate(action)
            }
        )
    }

    private fun handleRecyclerViewButton() {
        binding.apply {
            recyclerViewListenLater.addOnScrollListener(scrollListener(btnToTop))
            btnToTop.setOnClickListener {
                recyclerViewListenLater.smoothScrollToPosition(0)
            }
        }
    }

    private fun resetSearchState() {
        binding.apply {
            if (etSearch.text.isNullOrBlank() && !isSearchEditTextVisible.value) {
                linearLayoutSearch.isVisible = false
                etSearch.setText(getString(R.string.empty_string))
            } else {
                linearLayoutSearch.isVisible = true
            }
        }
    }

    private fun getRandomAlbum() {
        binding.btnListenLater.setOnClickListener {
            lifecycleScope.launch {
                viewModel.randomAlbum.collect { randomAlbum ->
                    if (randomAlbum != null) {
                        val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(randomAlbum.id)
                        findNavController().navigate(action)
                    } else {
                        showToast(requireContext(), requireContext().getString(R.string.first_add_something_to_list))
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewListenLater.apply {
            if (itemDecorationCount == 0) {
                addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))
            }
            layoutManager = createGridLayoutManager(requireContext())
            adapter = albumAdapter
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener { findNavController().navigateUp() }
            menu.findItem(R.id.imgAdd).setOnMenuItemClickListener {
                val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumSearchFragment()
                findNavController().navigate(action)
                true
            }
        }
    }

    override fun onPause() {
        super.onPause()
        savedSearchQuery = binding.etSearch.text.toString()
    }

    override fun onResume() {
        super.onResume()
        resetSearchState()
        savedSearchQuery?.let {
            binding.etSearch.setText(it)
        }
    }
}
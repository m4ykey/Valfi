package com.m4ykey.ui.album

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.m4ykey.album.ui.R
import com.m4ykey.album.ui.databinding.FragmentAlbumListenLaterBinding
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.album.adapter.AlbumAdapter
import com.m4ykey.ui.album.helpers.BooleanWrapper
import com.m4ykey.ui.album.helpers.createGridLayoutManager
import com.m4ykey.ui.album.helpers.hideSearchEditText
import com.m4ykey.ui.album.helpers.showSearchEditText
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
import com.m4ykey.ui.navigation.AlbumNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumListenLaterFragment : BaseFragment<FragmentAlbumListenLaterBinding>(
    FragmentAlbumListenLaterBinding::inflate
) {

    private val viewModel by viewModels<AlbumViewModel>()
    private val albumAdapter by lazy { createAlbumAdapter() }
    private var isSearchEditTextVisible = BooleanWrapper(false)
    private var isHidingAnimationRunning = BooleanWrapper(false)
    @Inject
    lateinit var navigator : AlbumNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        getRandomAlbum()
        handleRecyclerViewButton()

        bottomNavigationVisibility?.hideBottomNavigation()

        binding.apply {
            viewModel.apply {
                lifecycleScope.launch { getListenLaterCount() }
                lifecycleScope.launch { getListenLaterAlbums() }
                lifecycleScope.launch { getRandomAlbum() }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        albumEntity.collect { albums ->
                            handleAlbumDisplay(albums)
                        }
                    }
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        searchResult.collect { albums ->
                            if (!binding.etSearch.text.isNullOrEmpty()) {
                                handleSearchResult(albums)
                            }
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

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = etSearch.text.toString().trim()
                    if (query.isEmpty()) {
                        lifecycleScope.launch { viewModel.getListenLaterAlbums() }
                    } else {
                        lifecycleScope.launch { viewModel.searchAlbumsListenLater(query) }
                    }
                    true
                } else {
                    false
                }
            }

            chipSearch.setOnClickListener {
                isSearchEditTextVisible = showSearchEditText(isSearchEditTextVisible, linearLayoutSearch, -30f)
            }
        }
    }

    private fun handleSearchResult(albums : List<AlbumEntity>) {
        val newList = ArrayList(albums)

        if (binding.etSearch.text.isNullOrEmpty()) {
            binding.recyclerViewListenLater.post {
                albumAdapter.submitList(newList)
            }
            binding.linearLayoutEmptySearch.isVisible = false
            binding.linearLayoutEmptyList.isVisible = newList.isEmpty()
            return
        }
        if (newList.isEmpty()) {
            albumAdapter.submitList(emptyList())
            binding.linearLayoutEmptySearch.isVisible = true
            binding.linearLayoutEmptyList.isVisible = false
        } else {
            binding.recyclerViewListenLater.post {
                albumAdapter.submitList(newList)
            }
            binding.linearLayoutEmptySearch.isVisible = false
        }
    }

    private fun handleAlbumDisplay(albums : List<AlbumEntity>) {
        val newList = ArrayList(albums)

        if (newList.isEmpty()) {
            albumAdapter.submitList(emptyList())
            binding.linearLayoutEmptyList.isVisible = true
            binding.linearLayoutEmptySearch.isVisible = false
        } else {
            binding.recyclerViewListenLater.post {
                albumAdapter.submitList(newList)
            }
            binding.linearLayoutEmptyList.isVisible = false
            binding.linearLayoutEmptySearch.isVisible = false
        }
    }

    private fun createAlbumAdapter() : AlbumAdapter {
        return AlbumAdapter(
            onAlbumClick = { album ->
                if (findNavController().currentDestination?.id == R.id.albumListenLaterFragment) {
                    try {
                        navigator.navigateToAlbumDetail(album.id)
                    } catch (e : IllegalArgumentException) {
                        Log.e("NavigationError", "Navigation error: ${e.message}")
                    }
                } else {
                    Log.e("NavigationError", "Cannot navigate")
                }
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
                val randomAlbum = viewModel.randomAlbum.value
                if (randomAlbum != null) {
                    navigator.navigateToAlbumDetail(randomAlbum.id)
                } else {
                    showToast(requireContext(), requireContext().getString(R.string.first_add_something_to_list))
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val currentScrollPosition = (binding.recyclerViewListenLater.layoutManager as? GridLayoutManager)
            ?.findFirstVisibleItemPosition()

        binding.recyclerViewListenLater.apply {
            if (itemDecorationCount == 0) {
                addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))
            }
            layoutManager = createGridLayoutManager(requireContext())
            adapter = albumAdapter
            currentScrollPosition?.let { position ->
                layoutManager?.scrollToPosition(position)
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener { findNavController().navigateUp() }
            menu.findItem(R.id.imgAdd).setOnMenuItemClickListener {
                navigator.navigateToAlbumSearch()
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resetSearchState()
    }
}
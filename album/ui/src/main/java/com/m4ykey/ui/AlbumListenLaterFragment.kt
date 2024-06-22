package com.m4ykey.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.setupGridLayoutManager
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.ui.adapter.AlbumAdapter
import com.m4ykey.ui.databinding.FragmentAlbumListenLaterBinding
import com.m4ykey.ui.helpers.OnAlbumEntityClick
import com.m4ykey.ui.helpers.animationPropertiesY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class AlbumListenLaterFragment : BaseFragment<FragmentAlbumListenLaterBinding>(
    FragmentAlbumListenLaterBinding::inflate
) {

    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var albumAdapter : AlbumAdapter
    private var isSearchEditTextVisible = false
    private var isHidingAnimationRunning = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        setupToolbar()
        setupRecyclerView()
        getRandomAlbum()

        binding?.apply {
            viewModel.apply {
                lifecycleScope.launch { getListenLaterAlbums() }
                lifecycleScope.launch {
                    albumPaging.collect {
                        if (it.isEmpty()) {
                            albumAdapter.submitList(emptyList())
                            binding?.linearLayoutEmptyList?.isVisible = true
                            binding?.linearLayoutEmptySearch?.isVisible = false
                        } else {
                            binding?.linearLayoutEmptyList?.isVisible = false
                            if (binding?.etSearch?.text.isNullOrEmpty()) {
                                albumAdapter.submitList(it)
                                binding?.linearLayoutEmptySearch?.isVisible = false
                            }
                        }
                    }
                }

                binding?.etSearch?.doOnTextChanged { text, _, _, _ ->
                    if (text.isNullOrEmpty()) {
                        lifecycleScope.launch { getListenLaterAlbums() }
                    } else {
                        lifecycleScope.launch { searchAlbumsListenLater(text.toString()) }
                    }
                }

                lifecycleScope.launch {
                    searchResult.collect {
                        if (it.isEmpty()) {
                            albumAdapter.submitList(emptyList())
                            binding?.linearLayoutEmptySearch?.isVisible = true
                            binding?.linearLayoutEmptyList?.isVisible = false
                        } else {
                            albumAdapter.submitList(it)
                            binding?.linearLayoutEmptySearch?.isVisible = false
                        }
                    }
                }

                lifecycleScope.launch {
                    val albumCount = getListenLaterCount().firstOrNull() ?: 0
                    txtAlbumCount.text = getString(R.string.album_count, albumCount)
                }
            }

            imgHide.setOnClickListener {
                hideSearchEditText()
                etSearch.setText(getString(R.string.empty_string))
            }

            chipSearch.setOnClickListener { showSearchEditText() }
        }
    }

    private fun resetSearchState() {
        binding?.apply {
            if (etSearch.text.isNullOrBlank() && !isSearchEditTextVisible) {
                linearLayoutSearch.isVisible = false
                etSearch.setText(getString(R.string.empty_string))
            } else {
                linearLayoutSearch.isVisible = true
            }
        }
    }

    private fun showSearchEditText() {
        if (!isSearchEditTextVisible) {
            binding?.linearLayoutSearch?.apply {
                translationY = -30f
                isVisible = true
                animationPropertiesY(0f, 1f, DecelerateInterpolator())
            }
            isSearchEditTextVisible = true
        }
    }

    private fun hideSearchEditText() {
        if (isSearchEditTextVisible && !isHidingAnimationRunning) {
            isHidingAnimationRunning = true
            binding?.linearLayoutSearch?.apply {
                translationY = 0f
                animationPropertiesY(-30f, 0f, DecelerateInterpolator())
            }
            lifecycleScope.launch {
                delay(400)
                binding?.linearLayoutSearch?.isVisible = false
                isSearchEditTextVisible = false
                isHidingAnimationRunning = false
            }
        }
    }

    private fun getRandomAlbum() {
        binding?.btnListenLater?.setOnClickListener {
            lifecycleScope.launch {
                val randomAlbum = viewModel.getRandomAlbum()
                if (randomAlbum != null) {
                    val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(randomAlbum.id)
                    findNavController().navigate(action)
                } else {
                    showToast(requireContext(), requireContext().getString(R.string.first_add_something_to_list))
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.apply {

            val onAlbumClick : OnAlbumEntityClick = { album ->
                val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(album.id)
                findNavController().navigate(action)
            }

            albumAdapter = AlbumAdapter(onAlbumClick)

            recyclerViewListenLater.apply {
                addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))
                layoutManager = setupGridLayoutManager(requireContext(), 110f)
                adapter = albumAdapter
            }
        }
    }

    private fun setupToolbar() {
        binding?.toolbar?.apply {
            setNavigationOnClickListener { findNavController().navigateUp() }
            menu.findItem(R.id.imgAdd).setOnMenuItemClickListener {
                val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumSearchFragment()
                findNavController().navigate(action)
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resetSearchState()
    }
}
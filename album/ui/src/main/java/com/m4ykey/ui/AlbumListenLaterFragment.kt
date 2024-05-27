package com.m4ykey.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.show
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.AlbumPagingAdapter
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.databinding.FragmentAlbumListenLaterBinding
import com.m4ykey.ui.helpers.animationPropertiesY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumListenLaterFragment : Fragment() {

    private var _binding : FragmentAlbumListenLaterBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController : NavController
    private val viewModel : AlbumViewModel by viewModels()
    private lateinit var albumAdapter : AlbumPagingAdapter
    private var isSearchEditTextVisible = false
    private var isHidingAnimationRunning = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationVisibility) {
            bottomNavigationVisibility = context
        } else {
            throw RuntimeException("$context ${getString(R.string.must_implement_bottom_navigation)}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumListenLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        bottomNavigationVisibility?.hideBottomNavigation()

        with(binding) {
            setupToolbar()
            setupRecyclerView()
            getRandomAlbum()

            viewModel.apply {
                lifecycleScope.launch {
                    delay(500L)
                    getListenLaterAlbums()
                }
                albumPaging.observe(viewLifecycleOwner) { pagingData ->
                    albumAdapter.submitData(lifecycle, pagingData)
                }
                etSearch.doOnTextChanged { text, _, _, _ -> searchAlbumsListenLater(text.toString()) }
                searchResult.observe(viewLifecycleOwner) { pagingData ->
                    albumAdapter.submitData(lifecycle, pagingData)
                }
                lifecycleScope.launch {
                    val albumCount = getListenLaterCount().firstOrNull() ?: 0
                    txtAlbumCount.text = getString(R.string.album_count, albumCount)
                }
            }

            imgHide.setOnClickListener {
                hideSearchEditText()
                etSearch.setText("")
            }

            chipSearch.setOnClickListener { showSearchEditText() }
        }
    }

    private fun resetSearchState() {
        with(binding) {
            if (etSearch.text.isNullOrBlank() && !isSearchEditTextVisible) {
                linearLayoutSearch.isVisible = false
                etSearch.setText("")
            } else {
                linearLayoutSearch.isVisible = true
            }
        }
    }

    private fun showSearchEditText() {
        if (!isSearchEditTextVisible) {
            binding.linearLayoutSearch.apply {
                translationY = -30f
                show()
                animationPropertiesY(0f, 1f, DecelerateInterpolator())
            }
            isSearchEditTextVisible = true
        }
    }

    private fun hideSearchEditText() {
        if (isSearchEditTextVisible && !isHidingAnimationRunning) {
            isHidingAnimationRunning = true
            binding.linearLayoutSearch.apply {
                translationY = 0f
                animationPropertiesY(-30f, 0f, DecelerateInterpolator())
            }
            lifecycleScope.launch {
                delay(400)
                binding.linearLayoutSearch.hide()
                isSearchEditTextVisible = false
                isHidingAnimationRunning = false
            }
        }
    }

    private fun getRandomAlbum() {
        binding.btnListenLater.setOnClickListener {
            lifecycleScope.launch {
                val randomAlbum = viewModel.getRandomAlbum()
                if (randomAlbum != null) {
                    val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(randomAlbum.id)
                    navController.navigate(action)
                } else {
                    showToast(requireContext(), requireContext().getString(R.string.first_add_something_to_list))
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {

            val onAlbumClick : (AlbumEntity) -> Unit = { album ->
                val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(album.id)
                navController.navigate(action)
            }

            albumAdapter = AlbumPagingAdapter(onAlbumClick)

            recyclerViewListenLater.apply {
                addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = albumAdapter.withLoadStateHeaderAndFooter(
                    footer = LoadStateAdapter { albumAdapter.retry() },
                    header = LoadStateAdapter { albumAdapter.retry() }
                )
            }

            albumAdapter.addLoadStateListener { loadState ->
                val isEmpty = albumAdapter.itemCount == 0
                val isError = loadState.refresh is LoadState.Error
                linearLayoutEmptyList.isVisible = isEmpty && !isError
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener { navController.navigateUp() }
            menu.findItem(R.id.imgAdd).setOnMenuItemClickListener {
                val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumSearchFragment()
                navController.navigate(action)
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resetSearchState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
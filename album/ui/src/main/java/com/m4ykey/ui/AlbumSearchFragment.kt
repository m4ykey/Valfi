package com.m4ykey.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.show
import com.m4ykey.core.views.showToast
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.adapter.SearchAlbumPagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumSearchBinding
import com.m4ykey.ui.uistate.AlbumListUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumSearchFragment : Fragment(), OnItemClickListener<AlbumItem> {

    private var _binding : FragmentAlbumSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private var isClearButtonVisible = false
    private val viewModel : AlbumViewModel by viewModels()
    private val searchAdapter by lazy { SearchAlbumPagingAdapter(this, viewModel, requireContext()) }

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
        _binding = FragmentAlbumSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()
        navController = findNavController()

        with(binding) {
            setupToolbar()
            setupRecyclerView()
            searchAlbums()

            lifecycleScope.launch {
                viewModel.albums.observe(viewLifecycleOwner) { state -> handleSearchState(state) }
            }
        }
    }

    private fun handleSearchState(state : AlbumListUiState?) {
        with(binding) {
            rvSearchAlbums.isVisible = state?.isLoading == false && state.albumList != null
            progressBar.isVisible = state?.isLoading == true

            state?.error?.let {
                progressBar.isVisible = false
                showToast(requireContext(), it)
            }
            state?.albumList?.let { search ->
                progressBar.isVisible = false
                rvSearchAlbums.isVisible = true
                searchAdapter.submitData(lifecycle, search)
            }
        }
    }

    private fun FragmentAlbumSearchBinding.searchAlbums() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val searchQuery = etSearch.text.toString()
                    if (searchQuery.isNotEmpty()) {
                        lifecycleScope.launch { viewModel.searchAlbums(searchQuery) }
                    } else {
                        showToast(requireContext(), getString(R.string.empty_search))
                    }
                }
            }
            actionId == EditorInfo.IME_ACTION_SEARCH
        }
    }

    private fun FragmentAlbumSearchBinding.setupRecyclerView() {
        with(rvSearchAlbums) {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            val headerAdapter = LoadStateAdapter { searchAdapter.retry() }
            val footerAdapter = LoadStateAdapter { searchAdapter.retry() }

            adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )

            val layoutManager = GridLayoutManager(requireContext(), 3)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when {
                        position == 0 && headerAdapter.itemCount > 0 -> 3
                        position == adapter?.itemCount?.minus(1) && footerAdapter.itemCount > 0 -> 3
                        else -> 1
                    }
                }
            }

            this@with.layoutManager = layoutManager

            searchAdapter.addLoadStateListener { loadState ->
                handleLoadState(loadState)
            }
        }
    }

    private fun FragmentAlbumSearchBinding.handleLoadState(loadState : CombinedLoadStates) {
        progressBar.isVisible = loadState.source.refresh is LoadState.Loading

        val isNothingFound = loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                searchAdapter.itemCount < 1

        rvSearchAlbums.isVisible = !isNothingFound
        layoutNothingFound.root.isVisible = isNothingFound
    }

    private fun FragmentAlbumSearchBinding.setupToolbar() {
        with(toolbar) {
            setNavigationOnClickListener { navController.navigateUp() }
            etSearch.doOnTextChanged { text, _, _, _ ->
                val isSearchEmpty = text.isNullOrBlank()
                handleClearButtonVisibility(isSearchEmpty)
            }

            imgClear.setOnClickListener {
                etSearch.setText("")
                hideClearButtonWithAnimation()
            }
        }
    }

    private fun FragmentAlbumSearchBinding.handleClearButtonVisibility(isSearchEmpty : Boolean) {
        if (!isSearchEmpty && !isClearButtonVisible) {
            showClearButtonWithAnimation()
            isClearButtonVisible = true
        } else if (isSearchEmpty && isClearButtonVisible) {
            hideClearButtonWithAnimation()
            isClearButtonVisible = false
        }
    }

    private fun View.animationProperties(translationXValue : Float, alphaValue : Float, interpolator : Interpolator) {
        animate()
            .translationX(translationXValue)
            .alpha(alphaValue)
            .setInterpolator(interpolator)
            .start()
    }

    private fun FragmentAlbumSearchBinding.showClearButtonWithAnimation() {
        imgClear.apply {
            translationX = 100f
            alpha = 0f
            show()

            animationProperties(0f, 1f, DecelerateInterpolator())
        }
    }

    private fun FragmentAlbumSearchBinding.hideClearButtonWithAnimation() {
        imgClear.apply {
            animationProperties(width.toFloat(), 0f, AccelerateInterpolator())
        }
    }

    private fun FragmentAlbumSearchBinding.resetSearchState() {
        if (etSearch.text.isNullOrBlank()) {
            imgClear.isVisible = false
            isClearButtonVisible = false
        } else {
            imgClear.isVisible = true
            isClearButtonVisible = true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.resetSearchState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(position: Int, item: AlbumItem) {
        val albumId = item.id
        val action = AlbumSearchFragmentDirections.actionAlbumSearchFragmentToAlbumDetailFragment(albumId = albumId)
        findNavController().navigate(action)
    }
}
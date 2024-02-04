package com.m4ykey.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.isNightMode
import com.m4ykey.core.views.show
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.adapter.SearchAlbumPagingAdapter
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.databinding.FragmentAlbumSearchBinding
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
    private val searchAdapter by lazy { SearchAlbumPagingAdapter(this) }
    private val spaceBetweenItemsDp = 10

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

            viewModel.albums.observe(viewLifecycleOwner) { state ->
                lifecycleScope.launch {
                    searchAdapter.submitData(viewLifecycleOwner.lifecycle, state?.albumSearch ?: PagingData.empty())
                }
            }
        }
    }

    private fun FragmentAlbumSearchBinding.searchAlbums() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> lifecycleScope.launch { viewModel.searchAlbums(etSearch.text.toString()) }
            }
            actionId == EditorInfo.IME_ACTION_SEARCH
        }
    }

    private fun FragmentAlbumSearchBinding.setupRecyclerView() {
        with(rvSearchAlbums) {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(spaceBetweenItemsDp)))

            adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter(),
                footer = LoadStateAdapter()
            )

            searchAdapter.addLoadStateListener { loadState ->
                handleLoadState(loadState)
            }

            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun FragmentAlbumSearchBinding.handleLoadState(loadState : CombinedLoadStates) {
        val isLoading = loadState.source.refresh is LoadState.Loading
        progressBar.isVisible = isLoading

        val isNothingFound = loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                searchAdapter.itemCount < 1

        rvSearchAlbums.isVisible = !isNothingFound
        layoutNothingFound.root.isVisible = isNothingFound
    }

    private fun FragmentAlbumSearchBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), if (isNightMode) R.color.white else R.color.black)
        )

        with(toolbar) {
            imgClear.imageTintList = iconTint
            navigationIcon?.setTintList(iconTint)

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
        if (!isSearchEmpty && isClearButtonVisible) {
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
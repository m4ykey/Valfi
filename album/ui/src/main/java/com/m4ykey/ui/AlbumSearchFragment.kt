package com.m4ykey.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.isNightMode
import com.m4ykey.core.views.show
import com.m4ykey.ui.adapter.SearchAlbumLoadStateAdapter
import com.m4ykey.ui.adapter.SearchAlbumPagingAdapter
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.databinding.FragmentAlbumSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumSearchFragment : Fragment(), OnAlbumClick {

    private var _binding : FragmentAlbumSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private var isClearButtonVisible = false
    private val viewModel : AlbumViewModel by viewModels()
    private val searchAdapter by lazy { SearchAlbumPagingAdapter(this) }

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
                viewModel.albums.observe(viewLifecycleOwner) { albums ->
                    searchAdapter.submitData(viewLifecycleOwner.lifecycle, albums)
                }
            }
        }
    }

    private fun FragmentAlbumSearchBinding.searchAlbums() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                lifecycleScope.launch {
                    viewModel.searchAlbums(etSearch.text.toString())
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun FragmentAlbumSearchBinding.setupRecyclerView() {
        with(rvSearchAlbums) {
            setHasFixedSize(true)
            itemAnimator = null
            adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = SearchAlbumLoadStateAdapter(),
                footer = SearchAlbumLoadStateAdapter()
            )

            searchAdapter.addLoadStateListener { loadState ->
                rvSearchAlbums.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading

                val isNothingFound = loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        searchAdapter.itemCount < 1

                rvSearchAlbums.isVisible = !isNothingFound
                layoutNothingFound.root.isVisible = isNothingFound
            }

            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun FragmentAlbumSearchBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = if (isNightMode) {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
        }

        with(toolbar) {

            imgClear.imageTintList = iconTint
            navigationIcon?.setTintList(iconTint)

            setNavigationOnClickListener { navController.navigateUp() }
            etSearch.doOnTextChanged { text, _, _, _ ->
                val isSearchEmpty = text.isNullOrBlank()
                if (isSearchEmpty && isClearButtonVisible) {
                    hideClearButtonWithAnimation()
                    isClearButtonVisible = false
                } else if (!isSearchEmpty && !isClearButtonVisible) {
                    showClearButtonWithAnimation()
                    isClearButtonVisible = true
                }
            }

            imgClear.setOnClickListener {
                etSearch.setText("")
                hideClearButtonWithAnimation()
            }
        }
    }

    private fun FragmentAlbumSearchBinding.showClearButtonWithAnimation() {
        imgClear.apply {
            alpha = 0f
            translationX = 100f
            show()

            animate()
                .translationX(0f)
                .alpha(1f)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    alpha = 1f
                    translationX = 0f
                }
                .start()
        }
    }

    private fun FragmentAlbumSearchBinding.hideClearButtonWithAnimation() {
        imgClear.apply {
            animate()
                .translationX(width.toFloat())
                .alpha(0f)
                .setInterpolator(AccelerateInterpolator())
                .withEndAction {
                    alpha = 0f
                    translationX = width.toFloat()
                }
                .start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onAlbumClick(id: String) {
        val action = AlbumSearchFragmentDirections.actionAlbumSearchFragmentToAlbumDetailFragment(albumId = id)
        findNavController().navigate(action)
    }
}
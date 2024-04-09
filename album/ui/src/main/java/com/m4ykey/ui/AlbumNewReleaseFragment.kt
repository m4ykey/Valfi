package com.m4ykey.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.m4ykey.core.Constants
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.adapter.NewReleasePagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumNewReleaseBinding
import com.m4ykey.ui.uistate.AlbumListUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumNewReleaseFragment : Fragment(), OnItemClickListener<AlbumItem> {

    private var _binding : FragmentAlbumNewReleaseBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController : NavController
    private val viewModel : AlbumViewModel by viewModels()
    private val newReleaseAdapter by lazy { NewReleasePagingAdapter(this) }

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
        _binding = FragmentAlbumNewReleaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        bottomNavigationVisibility?.hideBottomNavigation()

        with(binding) {
            lifecycleScope.launch {
                viewModel.newRelease.observe(viewLifecycleOwner) { state -> handleNewReleaseState(state) }
            }

            toolbar.setNavigationOnClickListener { navController.navigateUp() }
            setupRecyclerView()
        }

    }

    private fun setupRecyclerView() {
        with(binding.recyclerViewNewRelease) {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(Constants.SPACE_BETWEEN_ITEMS)))

            val headerAdapter = LoadStateAdapter { newReleaseAdapter.retry() }
            val footerAdapter = LoadStateAdapter { newReleaseAdapter.retry() }

            adapter = newReleaseAdapter.withLoadStateHeaderAndFooter(
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
            newReleaseAdapter.addLoadStateListener { loadState -> handleLoadState(loadState) }
        }
    }

    private fun handleLoadState(loadState : CombinedLoadStates) {
        with(binding) {
            progressBar.isVisible = loadState.source.refresh is LoadState.Loading

            val isNothingFound = loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    newReleaseAdapter.itemCount < 1

            recyclerViewNewRelease.isVisible = !isNothingFound
        }
    }

    private fun handleNewReleaseState(state : AlbumListUiState?) {
        state ?: return
        with(binding) {
            progressBar.isVisible = state.isLoading
            recyclerViewNewRelease.isVisible = !state.isLoading
            state.error?.let { showToast(requireContext(), it) }
            state.albumList?.let { items ->
                newReleaseAdapter.submitData(lifecycle, items)
            }
        }
    }

    override fun onItemClick(position: Int, item: AlbumItem) {
        val action = AlbumNewReleaseFragmentDirections.actionAlbumNewReleaseFragmentToAlbumDetailFragment(item.id)
        navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.adapter.LoadStateAdapter
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.Article
import com.m4ykey.ui.adapter.NewsPagingAdapter
import com.m4ykey.ui.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private var _binding : FragmentNewsBinding? = null
    private val binding get() = _binding
    private val viewModel : NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsPagingAdapter

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
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding?.root ?: throw IllegalStateException("Binding root is null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.showBottomNavigation()

        binding?.apply {
            lifecycleScope.launch {
                delay(500L)
                viewModel.getMusicNews()
            }
            viewModel.news.observe(viewLifecycleOwner) { state -> handleNewsState(state) }
            setupRecyclerView()
        }

    }

    private fun setupRecyclerView() {
        binding?.recyclerViewNews?.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            newsAdapter = NewsPagingAdapter()

            adapter = newsAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { newsAdapter.retry() },
                footer = LoadStateAdapter { newsAdapter.retry() }
            )

            newsAdapter.addLoadStateListener { loadState -> handleLoadState(loadState)  }
        }
    }

    private fun handleLoadState(loadState : CombinedLoadStates) {
        binding?.apply {
            progressbar.isVisible = loadState.source.refresh is LoadState.Loading

            val isNothingFound = loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    newsAdapter.itemCount < 1

            recyclerViewNews.isVisible = !isNothingFound
        }
    }

    private fun handleNewsState(state : NewsUiState?) {
        state ?: return
        binding?.apply {
            progressbar.isVisible = state.isLoading
            recyclerViewNews.isVisible = !state.isLoading
            state.error?.let { showToast(requireContext(), it) }
            state.newsList?.let { items ->
                newsAdapter.submitData(lifecycle, items)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
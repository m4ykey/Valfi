package com.m4ykey.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.paging.handleLoadState
import com.m4ykey.core.views.BaseFragment
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
class NewsFragment : BaseFragment<FragmentNewsBinding>(
    FragmentNewsBinding::inflate
) {

    private val viewModel by viewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.showBottomNavigation()

        lifecycleScope.launch {
            delay(500L)
            viewModel.getMusicNews()
        }
        viewModel.news.observe(viewLifecycleOwner) { state -> handleNewsState(state) }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.recyclerViewNews?.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            val onNewsClick : (Article) -> Unit = { article ->
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(requireContext(), Uri.parse(article.url))
            }

            newsAdapter = NewsPagingAdapter(onNewsClick)

            adapter = newsAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { newsAdapter.retry() },
                footer = LoadStateAdapter { newsAdapter.retry() }
            )

            newsAdapter.addLoadStateListener { loadState ->
                handleLoadState(
                    loadState = loadState,
                    progressBar = binding!!.progressbar,
                    recyclerView = this,
                    adapter = newsAdapter
                )
            }
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
}
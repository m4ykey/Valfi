package com.m4ykey.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.network.ErrorState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.Article
import com.m4ykey.ui.adapter.NewsAdapter
import com.m4ykey.ui.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>(
    FragmentNewsBinding::inflate
) {

    private val viewModel by viewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.showBottomNavigation()

        setupRecyclerView()
        setupToolbar()

        lifecycleScope.launch {
            viewModel.getMusicNews()
        }

        lifecycleScope.launch {
            viewModel.news.collect { newsAdapter.submitList(it) }
        }

        lifecycleScope.launch {
            viewModel.isError.collect { errorState ->
                when (errorState) {
                    is ErrorState.Error -> showToast(requireContext(), errorState.message.toString())
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding?.progressbar?.isVisible = it
            }
        }

        binding?.recyclerViewNews?.addOnScrollListener(scrollListener(binding?.btnToTop!!))

        binding?.btnToTop?.setOnClickListener {
            binding?.recyclerViewNews?.smoothScrollToPosition(0)
        }
    }

    private fun setupToolbar() {
        binding?.apply {
            val buttons = listOf(
                Pair(R.id.publishedAt) {

                },
                Pair(R.id.popularity) {

                }
            )
            for ((itemId, action) in buttons) {
                toolbar.menu.findItem(itemId)?.setOnMenuItemClickListener {
                    action.invoke()
                    true
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerViewNews?.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            val onNewsClick : (Article) -> Unit = { article ->
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(requireContext(), Uri.parse(article.url))
            }

            newsAdapter = NewsAdapter(onNewsClick)
            adapter = newsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        if (!viewModel.isPaginationEnded && !viewModel.isLoading.value) {
                            lifecycleScope.launch { viewModel.getMusicNews() }
                        }
                    }
                }
            })
        }
    }
}
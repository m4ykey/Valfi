package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.openUrlBrowser
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.preferences.ListType
import com.m4ykey.data.preferences.NewsPreferences
import com.m4ykey.ui.adapter.NewsAdapter
import com.m4ykey.ui.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>(
    FragmentNewsBinding::inflate
) {

    private val viewModel by viewModels<NewsViewModel>()
    private val newsAdapter by lazy { createNewsAdapter() }
    private var sortBy : String = "publishedAt"
    private var isListViewChanged = false
    @Inject
    lateinit var newsPreferences: NewsPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.showBottomNavigation()

        fetchNews(clearList = true)
        setupRecyclerView()
        setupToolbar()
        handleRecyclerViewButton()

        lifecycleScope.launch {
            viewModel.news.collect { newsAdapter.submitList(it) }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let { showToast(requireContext(), it) }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.progressbar.isVisible = it
            }
        }
    }

    private fun setRecyclerViewLayout(isListView : Boolean) {
        val listType = if (isListView) ListType.LIST else ListType.TABLE

        val currentScrollPosition = (binding.recyclerViewNews.layoutManager as? LinearLayoutManager)
            ?.findFirstVisibleItemPosition()

        binding.recyclerViewNews.apply {
            newsAdapter.listType = listType

            currentScrollPosition?.let { position ->
                layoutManager?.scrollToPosition(position)
            }
        }
    }

    private fun fetchNews(clearList : Boolean) {
        lifecycleScope.launch {
            if (viewModel.news.value.isEmpty() || clearList) {
                viewModel.getMusicNews(clearList = clearList, sortBy = sortBy)
            }
        }
    }

    private fun handleRecyclerViewButton() {
        binding.apply {
            recyclerViewNews.addOnScrollListener(scrollListener(btnToTop))
            btnToTop.setOnClickListener {
                recyclerViewNews.smoothScrollToPosition(0)
            }
        }
    }

    private fun createNewsAdapter() : NewsAdapter {
        return NewsAdapter(
            onNewsClick = { article -> openUrlBrowser(requireContext(), article.url) }
        )
    }

    private fun setupToolbar() {
        binding.apply {
            val buttons = listOf(
                Pair(R.id.publishedAt) {
                    sortBy = "publishedAt"
                    txtTitle.text = getString(R.string.latest_news)
                    fetchNews(true)
                },
                Pair(R.id.popularity) {
                    sortBy = "popularity"
                    txtTitle.text = getString(R.string.popular_news)
                    fetchNews(true)
                }
            )
            for ((itemId, action) in buttons) {
                toolbar.menu.findItem(itemId)?.setOnMenuItemClickListener {
                    action.invoke()
                    true
                }
            }
            toolbar.menu.findItem(R.id.viewType).setOnMenuItemClickListener {
                isListViewChanged = !isListViewChanged
                lifecycleScope.launch {
                    if (isListViewChanged) {
                        newsPreferences.saveSelectedListType(ListType.LIST)
                        toolbar.menu.findItem(R.id.viewType).setIcon(R.drawable.ic_table_row)
                    } else {
                        newsPreferences.deleteSelectedListType()
                        toolbar.menu.findItem(R.id.viewType).setIcon(R.drawable.ic_list)
                    }
                    setRecyclerViewLayout(isListViewChanged)
                }
                true
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewNews.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))
            adapter = newsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        if (!viewModel.isPaginationEnded && !viewModel.isLoading.value) {
                            lifecycleScope.launch { viewModel.getMusicNews(false, sortBy) }
                        }
                    }
                }
            })
        }
    }

    private fun readSelectedListType() {
        lifecycleScope.launch {
            newsPreferences.getSelectedListType().collect { selectedListType ->
                binding.apply {
                    if (selectedListType != null) {
                        newsAdapter.listType = selectedListType
                        toolbar.menu.findItem(R.id.viewType).setIcon(R.drawable.ic_table_row)
                        isListViewChanged = selectedListType == ListType.LIST
                    } else {
                        newsAdapter.listType = ListType.TABLE
                        toolbar.menu.findItem(R.id.viewType).setIcon(R.drawable.ic_list)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        readSelectedListType()
    }
}
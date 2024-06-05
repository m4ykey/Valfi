package com.m4ykey.core.paging

import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

fun handleLoadState(
    loadState: CombinedLoadStates,
    recyclerView: RecyclerView,
    progressBar: ProgressBar,
    adapter: PagingDataAdapter<*, *>
) {
    progressBar.isVisible = loadState.source.refresh is LoadState.Loading

    val isNothingFound = loadState.source.refresh is LoadState.NotLoading &&
            loadState.append.endOfPaginationReached &&
            adapter.itemCount < 1

    recyclerView.isVisible = !isNothingFound
}
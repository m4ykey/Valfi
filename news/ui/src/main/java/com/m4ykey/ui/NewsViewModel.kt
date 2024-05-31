package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.m4ykey.core.paging.launchPaging
import com.m4ykey.data.domain.model.Article
import com.m4ykey.data.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var _news = MutableLiveData(NewsUiState())
    val news : LiveData<NewsUiState> get() = _news

    fun getMusicNews() {
        launchPaging(
            scope = viewModelScope,
            source = { repository.getMusicNews() },
            onDataCollected = { pagingData : PagingData<Article> ->
                val newState = NewsUiState(newsList = pagingData)
                _news.value = newState
            }
        )
    }
}
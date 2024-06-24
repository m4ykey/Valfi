package com.m4ykey.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.ErrorState
import com.m4ykey.data.domain.model.Article
import com.m4ykey.data.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var _news = MutableStateFlow<List<Article>>(emptyList())
    val news : StateFlow<List<Article>> get() = _news

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading

    private val _isError = MutableStateFlow<ErrorState>(ErrorState.NoError)
    val isError : StateFlow<ErrorState> get() = _isError

    private val _currentSort = MutableStateFlow(NewsSort.PUBLISHED_AT)
    val currentSort : StateFlow<NewsSort> get() = _currentSort

    private var page = 1
    var isPaginationEnded = false

    init {
        viewModelScope.launch {
            _currentSort.collect {
                getMusicNews(clearList = true)
            }
        }
    }

    fun setCurrentSort(sort: NewsSort) {
        _currentSort.value = sort
    }

    suspend fun getMusicNews(clearList : Boolean) = viewModelScope.launch {
        if (_isLoading.value || isPaginationEnded) return@launch

        if (clearList) {
            _news.value = emptyList()
            page = 1
            isPaginationEnded = false
        }

        _isLoading.value = true
        _isError.value = ErrorState.NoError
        try {
            repository.getMusicNews(page = page, pageSize = PAGE_SIZE, sortBy = _currentSort.value.name)
                .collect { articles ->
                    if (articles.isNotEmpty()) {
                        _news.value += articles
                        page++
                    } else {
                        isPaginationEnded = true
                    }
                }
        } catch (e : Exception) {
            _isError.value = ErrorState.Error(e.message ?: "Unknown error")
        } finally {
            _isLoading.value = false
        }
    }
}
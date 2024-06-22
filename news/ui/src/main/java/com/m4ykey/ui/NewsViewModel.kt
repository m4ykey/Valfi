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

    private var page = 1
    var isPaginationEnded = false

    suspend fun getMusicNews() = viewModelScope.launch {
        if (_isLoading.value || isPaginationEnded) return@launch

        _isLoading.value = true
        _isError.value = ErrorState.NoError
        try {
            repository.getMusicNews(page = page, pageSize = PAGE_SIZE)
                .collect { articles ->
                    if (articles.isNotEmpty()) {
                        val currentList = _news.value.toMutableList()
                        currentList.addAll(articles)
                        _news.value = currentList
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
package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.data.domain.model.Article
import com.m4ykey.data.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var _news = MutableLiveData<List<Article>>()
    val news : LiveData<List<Article>> get() = _news

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> get() = _isError

    private var page = 1
    var isPaginationEnded = false

    suspend fun getMusicNews() {
        if (_isLoading.value == true || isPaginationEnded) return

        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                repository.getMusicNews(page = page, pageSize = PAGE_SIZE)
                    .collect { articles ->
                        if (articles.isNotEmpty()) {
                            val currentList = _news.value?.toMutableList() ?: mutableListOf()
                            currentList.addAll(articles)
                            _news.value = currentList
                            page++
                        } else {
                            isPaginationEnded = true
                        }
                    }
            } catch (e : Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}
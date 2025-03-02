package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.data.domain.repository.SearchResultRepository
import com.m4ykey.data.local.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val repository: SearchResultRepository,
    private val dispatcherIO : CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _searchResult = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    fun getSearchResult() {
        viewModelScope.launch(dispatcherIO) {
            val searchResult = repository.getSearchResult()
            searchResult.collect { result ->
                _searchResult.value = result.take(10)
            }
        }
    }

    fun insertSearchResult(searchResult: SearchResult) {
        viewModelScope.launch(dispatcherIO) {
            repository.insertSearchResult(searchResult)
        }
    }

    fun deleteSearchResults() {
        viewModelScope.launch(dispatcherIO) {
            repository.deleteSearchResults()
        }
    }

}
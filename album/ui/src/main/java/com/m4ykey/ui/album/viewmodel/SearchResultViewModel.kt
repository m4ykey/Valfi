package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.data.domain.repository.SearchResultRepository
import com.m4ykey.data.local.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val repository: SearchResultRepository
) : ViewModel() {

    private val _searchResult = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResult : StateFlow<List<SearchResult>> get() = _searchResult

    fun getSearchResult() {
        viewModelScope.launch {
            val searchResult = repository.getSearchResult()
            searchResult.collect { result ->
                _searchResult.value = result.take(10)
            }
        }
    }

    fun insertSearchResult(searchResult: SearchResult) {
        viewModelScope.launch {
            repository.insertSearchResult(searchResult)
        }
    }

    suspend fun deleteSearchResults() {
        repository.deleteSearchResults()
    }

}
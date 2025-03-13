package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.data.domain.repository.SearchResultRepository
import com.m4ykey.data.domain.usecase.searchresult.DeleteSearchResultUseCase
import com.m4ykey.data.domain.usecase.searchresult.SaveSearchResultUseCase
import com.m4ykey.data.domain.usecase.searchresult.SearchResultUseCase
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
    private val useCase : SearchResultUseCase,
    private val saveSearchResultUseCase: SaveSearchResultUseCase,
    private val deleteSearchResultUseCase: DeleteSearchResultUseCase,
    private val dispatcherIO : CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _searchResult = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    fun getSearchResult() {
        viewModelScope.launch(dispatcherIO) {
            val searchResult = useCase.getSearchResult()
            searchResult.collect { result ->
                _searchResult.value = result.take(10)
            }
        }
    }

    fun insertSearchResult(searchResult: SearchResult) {
        viewModelScope.launch(dispatcherIO) {
            saveSearchResultUseCase.insertSearchResult(searchResult)
        }
    }

    fun deleteSearchResults() {
        viewModelScope.launch(dispatcherIO) {
            deleteSearchResultUseCase.deleteSearchResults()
        }
    }

}
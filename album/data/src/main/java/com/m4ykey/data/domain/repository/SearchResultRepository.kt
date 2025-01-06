package com.m4ykey.data.domain.repository

import com.m4ykey.data.local.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchResultRepository {

    suspend fun insertSearchResult(searchResult: SearchResult)
    suspend fun deleteSearchResults()
    fun getSearchResult() : Flow<List<SearchResult>>

}
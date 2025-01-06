package com.m4ykey.data.repository

import com.m4ykey.data.domain.repository.SearchResultRepository
import com.m4ykey.data.local.dao.SearchResultDao
import com.m4ykey.data.local.model.SearchResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val dao : SearchResultDao
) : SearchResultRepository {

    override suspend fun insertSearchResult(searchResult: SearchResult) {
        return dao.insertSearchResult(searchResult)
    }

    override suspend fun deleteSearchResults() {
        return dao.deleteSearchResults()
    }

    override fun getSearchResult(): Flow<List<SearchResult>> {
        return dao.getSearchResult()
    }
}
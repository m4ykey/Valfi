package com.m4ykey.data.domain.usecase.searchresult

import com.m4ykey.data.domain.repository.SearchResultRepository
import com.m4ykey.data.local.model.SearchResult
import javax.inject.Inject

class SaveSearchResultUseCase @Inject constructor(
    private val repository: SearchResultRepository
) {

    suspend fun insertSearchResult(searchResult : SearchResult) = repository.insertSearchResult(searchResult)

}
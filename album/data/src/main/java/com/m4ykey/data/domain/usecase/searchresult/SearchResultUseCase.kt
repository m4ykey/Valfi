package com.m4ykey.data.domain.usecase.searchresult

import com.m4ykey.core.UseCase
import com.m4ykey.data.domain.repository.SearchResultRepository
import com.m4ykey.data.local.model.SearchResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchResultUseCase @Inject constructor(
    private val repository: SearchResultRepository
)  {

    fun getSearchResult() : Flow<List<SearchResult>> = repository.getSearchResult()

}
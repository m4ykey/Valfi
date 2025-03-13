package com.m4ykey.data.domain.usecase.searchresult

import com.m4ykey.data.domain.repository.SearchResultRepository
import javax.inject.Inject

class DeleteSearchResultUseCase @Inject constructor(
    private val repository: SearchResultRepository
) {

    suspend fun deleteSearchResults() = repository.deleteSearchResults()

}
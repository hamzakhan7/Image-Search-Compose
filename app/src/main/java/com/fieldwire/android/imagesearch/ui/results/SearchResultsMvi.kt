package com.fieldwire.android.imagesearch.ui.results

import com.fieldwire.android.imagesearch.api.ImageDetail

sealed class SearchResultsState {
    object Loading : SearchResultsState()
    data class Success(
        val images: List<ImageDetail>,
        val page: Int,
        val isLoadingMore: Boolean = false
    ) : SearchResultsState()
    data class Error(val message: String) : SearchResultsState()
}

sealed class SearchResultsEvent {
    data class Search(val query: String) : SearchResultsEvent()
    object LoadMore : SearchResultsEvent()
}

package com.fieldwire.android.imagesearch.ui.results

import com.fieldwire.android.imagesearch.api.GalleryItem

sealed class SearchResultsState{
    object Loading: SearchResultsState()
    data class Success(val images: List<GalleryItem>) : SearchResultsState()
    data class Error(val message: String) : SearchResultsState()
}

sealed class SearchResultsEvent {
    data class Search(val query: String) : SearchResultsEvent()
}
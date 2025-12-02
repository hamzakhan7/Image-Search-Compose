package com.fieldwire.android.imagesearch.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fieldwire.android.imagesearch.api.ImageDetail
import com.fieldwire.android.imagesearch.data.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchResultsState>(SearchResultsState.Loading)
    val state: StateFlow<SearchResultsState> = _state.asStateFlow()

    private var currentQuery = ""

    fun onEvent(event: SearchResultsEvent) {
        when (event) {
            is SearchResultsEvent.Search -> newSearch(event.query)
            is SearchResultsEvent.LoadMore -> loadMore()
        }
    }

    private fun newSearch(query: String) {
        currentQuery = query
        _state.value = SearchResultsState.Loading
        viewModelScope.launch {
            try {
                val images = imageRepository.searchGallery(query = currentQuery, page = 1)
                _state.value = SearchResultsState.Success(images = images, page = 1)
            } catch (e: Exception) {
                _state.value = SearchResultsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState !is SearchResultsState.Success || currentState.isLoadingMore) {
                return@launch
            }

            _state.value = currentState.copy(isLoadingMore = true)

            val nextPage = currentState.page + 1
            try {
                val newImages = imageRepository.searchGallery(query = currentQuery, page = nextPage)
                val allImages = currentState.images + newImages
                _state.value = SearchResultsState.Success(
                    images = allImages,
                    page = nextPage,
                    isLoadingMore = false
                )
            } catch (e: Exception) {
                _state.value = currentState.copy(isLoadingMore = false)
            }
        }
    }
}

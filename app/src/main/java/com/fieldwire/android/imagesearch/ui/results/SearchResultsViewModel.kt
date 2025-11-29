package com.fieldwire.android.imagesearch.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun onEvent(event: SearchResultsEvent) {
        when (event) {
            is SearchResultsEvent.Search -> search(event.query)
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            _state.value = SearchResultsState.Loading
            try {
                val response = imageRepository.searchGallery(query)
                _state.value = SearchResultsState.Success(response.data)
            } catch (e: Exception) {
                _state.value = SearchResultsState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

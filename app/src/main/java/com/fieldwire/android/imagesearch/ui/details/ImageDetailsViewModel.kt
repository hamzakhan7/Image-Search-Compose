package com.fieldwire.android.imagesearch.ui.details

import androidx.compose.ui.window.Popup
import androidx.lifecycle.SavedStateHandle
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
class ImageDetailsViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<ImageDetailsState>(ImageDetailsState.Loading)
    val state: StateFlow<ImageDetailsState>
        get() = _state.asStateFlow()

    init {
        val imageId = savedStateHandle.get<String>("imageId")
        if (imageId == null) {
            _state.value = ImageDetailsState.Error("Invalid image ID")
        } else {
            loadImage(imageId)
        }
    }

    private fun loadImage(imageId: String){
        viewModelScope.launch {
            val image = imageRepository.getImage(imageId)
            if (image == null) {
                _state.value = ImageDetailsState.Error("Image not found")
            } else {
                _state.value = ImageDetailsState.Success(image)
            }
        }
    }
}
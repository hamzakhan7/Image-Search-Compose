package com.fieldwire.android.imagesearch.ui.details

import com.fieldwire.android.imagesearch.api.ImageDetail

sealed class ImageDetailsState {
    object Loading : ImageDetailsState()
    data class Success(val image: ImageDetail) : ImageDetailsState()
    data class Error(val message: String) : ImageDetailsState()
}

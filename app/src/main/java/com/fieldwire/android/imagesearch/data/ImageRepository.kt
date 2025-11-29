package com.fieldwire.android.imagesearch.data

import com.fieldwire.android.imagesearch.api.ImgurApi
import javax.inject.Inject

class ImageRepository @Inject constructor(private val imgurApi: ImgurApi) {
    suspend fun searchGallery(query: String) = imgurApi.searchGallery(query = query)
}
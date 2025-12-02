package com.fieldwire.android.imagesearch.data

import com.fieldwire.android.imagesearch.api.GalleryItem
import com.fieldwire.android.imagesearch.api.ImageDetail
import com.fieldwire.android.imagesearch.api.ImgurApi
import com.fieldwire.android.imagesearch.api.asImageDetail
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(private val imgurApi: ImgurApi) {

    private val imageCache = mutableMapOf<String, ImageDetail>()

    suspend fun searchGallery(query: String, page: Int): List<ImageDetail> {
        val response = imgurApi.searchGallery(query = query, page = page)
        val newImages = transformToImageDetail(response.data)

        if (page == 1) {
            imageCache.clear()
        }
        imageCache.putAll(newImages.associateBy { it.id })
        return newImages
    }

    fun getImage(id: String): ImageDetail? {
        return imageCache[id]
    }

    private fun transformToImageDetail(items: List<GalleryItem>): List<ImageDetail> {
        return items
            .mapNotNull {
                if (it.isAlbum) {
                    it.images?.firstOrNull()
                } else {
                    it.asImageDetail()
                }
            }
            .filter { imageDetail ->
                val link = imageDetail.link
                link.endsWith(".jpg", ignoreCase = true) || link.endsWith(".png", ignoreCase = true)
            }
    }
}

package com.fieldwire.android.imagesearch.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurApi {
    @GET("gallery/search/{sort}/{window}/{page}")
    suspend fun searchGallery(
        @Path("sort") sort: String = "time",
        @Path("window") window: String = "all",
        @Path("page") page: Int = 1,
        @Query("q") query: String,
        @Query("q_type") qType: String = "jpg|png"
    ): ImgurResponse

    companion object {
        const val BASE_URL = "https://api.imgur.com/3/"
        const val CLIENT_ID = "b067d5cb828ec5a"
    }
}

fun GalleryItem.asImageDetail(): ImageDetail {
    val imageWidth = this.width ?: this.coverWidth ?: 0
    val imageHeight = this.height ?: this.coverHeight ?: 0

    return ImageDetail(
        id = this.id,
        title = this.title,
        width = imageWidth,
        height = imageHeight,
        link = this.link
    )
}

data class ImgurResponse(
    val data: List<GalleryItem>,
    val success: Boolean,
    val status: Int
)

data class GalleryItem(
    val id: String,
    val title: String?,
    @SerializedName("cover_width")
    val coverWidth: Int?,
    @SerializedName("cover_height")
    val coverHeight: Int?,
    val link: String,
    @SerializedName("is_album")
    val isAlbum: Boolean,
    val images: List<ImageDetail>?,
    val width: Int?,
    val height: Int?,
)

data class ImageDetail(
    val id: String,
    val title: String?,
    val width: Int,
    val height: Int,
    val link: String,
)

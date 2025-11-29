package com.fieldwire.android.imagesearch.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

interface ImgurApi {
    @GET("gallery/search/{sort}/{window}/{page}")
    suspend fun searchGallery(
        @Path("sort") sort: String = "viral",
        @Path("window") window: String = "all",
        @Path("page") page: Int = 1,
        @Query("q") query: String
    ): ImgurResponse
}

data class ImgurResponse(
    val data: List<GalleryItem>,
    val success: Boolean,
    val status: Int
)

data class GalleryItem(
    val id: String,
    val title: String?,
    val description: String?,
    val datetime: Long,
    val cover: String?,
    @SerializedName("cover_width")
    val coverWidth: Int?,
    @SerializedName("cover_height")
    val coverHeight: Int?,
    @SerializedName("account_url")
    val accountUrl: String?,
    @SerializedName("account_id")
    val accountId: Int?,
    val privacy: String?,
    val layout: String?,
    val views: Int,
    val link: String,
    val ups: Int,
    val downs: Int,
    val points: Int,
    val score: Int,
    @SerializedName("is_album")
    val isAlbum: Boolean,
    val vote: String?,
    val favorite: Boolean,
    val nsfw: Boolean,
    val section: String,
    @SerializedName("comment_count")
    val commentCount: Int,
    @SerializedName("favorite_count")
    val favoriteCount: Int,
    val topic: String?,
    @SerializedName("topic_id")
    val topicId: Int?,
    @SerializedName("images_count")
    val imagesCount: Int,
    @SerializedName("in_gallery")
    val inGallery: Boolean,
    @SerializedName("is_ad")
    val isAd: Boolean,
    val tags: List<Tag>,
    val images: List<ImageDetail>?
)

data class ImageDetail(
    val id: String,
    val title: String?,
    val description: String?,
    val datetime: Long,
    val type: String,
    val animated: Boolean,
    val width: Int,
    val height: Int,
    val size: Long,
    val views: Int,
    val bandwidth: Long,
    val vote: String?,
    val favorite: Boolean,
    val nsfw: Boolean?,
    val section: String?,
    @SerializedName("account_url")
    val accountUrl: String?,
    @SerializedName("account_id")
    val accountId: Int?,
    @SerializedName("is_ad")
    val isAd: Boolean,
    @SerializedName("in_most_viral")
    val inMostViral: Boolean,
    @SerializedName("has_sound")
    val hasSound: Boolean,
    val tags: List<Tag>,
    val link: String,
    val mp4: String?,
    val gifv: String?,
    val hls: String?,
    @SerializedName("mp4_size")
    val mp4Size: Int?
)

data class Tag(
    val name: String,
    @SerializedName("display_name")
    val displayName: String,
    val followers: Int,
    @SerializedName("total_items")
    val totalItems: Int,
    val following: Boolean
)

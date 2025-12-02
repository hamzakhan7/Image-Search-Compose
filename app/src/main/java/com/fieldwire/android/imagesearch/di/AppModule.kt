package com.fieldwire.android.imagesearch.di

import com.fieldwire.android.imagesearch.api.ImgurApi
import com.fieldwire.android.imagesearch.api.ImgurApi.Companion.BASE_URL
import com.fieldwire.android.imagesearch.api.ImgurApi.Companion.CLIENT_ID
import com.fieldwire.android.imagesearch.data.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient() : OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Client-ID $CLIENT_ID")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providerImgurApi(retrofit: Retrofit) : ImgurApi {
        return retrofit.create(ImgurApi::class.java)
    }

    @Provides
    @Singleton
    fun provideImageRepository(imgurApi: ImgurApi): ImageRepository {
        return ImageRepository(imgurApi)
    }
}
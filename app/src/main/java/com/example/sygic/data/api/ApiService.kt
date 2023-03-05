package com.example.sygic.data.api

import com.example.sygic.data.model.GalleryImage
import com.example.sygic.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("x-api-key: live_vayLeEleBoZjnLDMH4fAAYuamIErnRk5khqYjkAUrxJqMDxBsJAZ61WSRIOdenCE")
    @GET("v1/images/search")
    suspend fun getImages(
        @Query("has_breeds") hasBreeds: Int = 1,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE,
        @Query("page") page: Int
    ): List<GalleryImage>
}
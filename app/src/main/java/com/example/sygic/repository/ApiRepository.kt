package com.example.sygic.repository

import androidx.paging.PagingData
import com.example.sygic.data.model.GalleryItem
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    fun getImages(): Flow<PagingData<GalleryItem>>
}
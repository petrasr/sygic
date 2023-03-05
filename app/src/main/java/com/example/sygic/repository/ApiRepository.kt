package com.example.sygic.repository

import androidx.paging.PagingData
import com.example.sygic.data.model.GalleryImage
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    fun getImages(): Flow<PagingData<GalleryImage>>
}
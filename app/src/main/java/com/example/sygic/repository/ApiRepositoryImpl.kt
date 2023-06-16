package com.example.sygic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.sygic.data.api.ApiService
import com.example.sygic.data.model.GalleryItem
import com.example.sygic.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
  private val apiService: ApiService
) : ApiRepository {

    override fun getImages(): Flow<PagingData<GalleryItem>> =
        Pager(
            config = getPagingConfig(),
            pagingSourceFactory = { GalleryPagingSource(apiService) }
        ).flow

    private fun getPagingConfig(): PagingConfig =
        PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE, prefetchDistance = 2)
}
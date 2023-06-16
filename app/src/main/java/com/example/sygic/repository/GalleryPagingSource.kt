package com.example.sygic.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sygic.data.api.ApiService
import com.example.sygic.data.model.GalleryImage
import com.example.sygic.data.model.GalleryItem
import javax.inject.Inject

class GalleryPagingSource @Inject constructor(
    private val apiService: ApiService
    ) : PagingSource<Int, GalleryItem>() {

    companion object {
        private const val DEFAULT_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, GalleryItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getImages(page = page).map { it.toGalleryItem() }
            LoadResult.Page(
                response,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page.minus(1),
                nextKey = if (response.isEmpty()) null else page.plus(1)
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private fun GalleryImage.toGalleryItem(): GalleryItem =
        GalleryItem(
            id,
            url,
            breeds.firstOrNull()?.name,
            breeds.firstOrNull()?.origin
        )
}
package com.example.sygic.ui

import androidx.paging.PagingSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sygic.data.api.ApiService
import com.example.sygic.data.model.GalleryImage
import com.example.sygic.repository.GalleryPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    private val mockData = listOf(
        GalleryImage(emptyList(), "id1", "url1"),
        GalleryImage(emptyList(), "id2", "url2"),
        GalleryImage(emptyList(), "id3", "url3"),
        GalleryImage(emptyList(), "id4", "url4"),
    )

    private val mockApi = MockApi(mockData)

    @Test
    fun returnsPageSuccessfullyNextPage() = runTest {
        val pagingSource = GalleryPagingSource(mockApi)
        val expected = PagingSource.LoadResult.Page(
            data = mockData,
            prevKey = 1,
            nextKey = 3
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 2,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun returnsPageSuccessfullyFirstPage() = runTest {
        val pagingSource = GalleryPagingSource(mockApi)
        val expected = PagingSource.LoadResult.Page(
            data = mockData,
            prevKey = null,
            nextKey = 2
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        assertEquals(expected, actual)
    }

    private class MockApi(private val mockData: List<GalleryImage>) : ApiService {
        override suspend fun getImages(
            hasBreeds: Int,
            limit: Int,
            page: Int
        ): List<GalleryImage> = mockData
    }
}
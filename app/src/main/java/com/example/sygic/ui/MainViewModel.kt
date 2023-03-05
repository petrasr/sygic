package com.example.sygic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.sygic.data.model.GalleryItem
import com.example.sygic.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty

class MainViewModel(apiRepository: ApiRepository) : ViewModel() {

    val gallery: Flow<PagingData<GalleryItem>> =
        apiRepository.getImages()
            .map {
                it.map { image ->
                    GalleryItem(
                        image.id,
                        image.url,
                        image.breeds.firstOrNull()?.name,
                        image.breeds.firstOrNull()?.origin
                    )
                }
            }
            .cachedIn(viewModelScope)
}

@Suppress("UNCHECKED_CAST")
class MainVMF(private val apiRepository: ApiRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(apiRepository) as T
}
package com.example.sygic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sygic.data.model.GalleryItem
import com.example.sygic.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    apiRepository: ApiRepository
) : ViewModel() {

    val gallery: Flow<PagingData<GalleryItem>> =
        apiRepository.getImages().cachedIn(viewModelScope)
}
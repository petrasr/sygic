package com.example.sygic.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.sygic.R
import com.example.sygic.data.api.RetrofitBuilder
import com.example.sygic.databinding.ActivityMainBinding
import com.example.sygic.repository.ApiRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainVMF(ApiRepositoryImpl(RetrofitBuilder.apiService))
    }
    private val adapter = GalleryAdapter(::onItemClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val stateAdapter = GalleryStateAdapter { adapter.retry() }
        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(stateAdapter)
            reloadButton.setOnClickListener { adapter.refresh() }
            swipeRefresh.setOnRefreshListener { adapter.refresh() }
        }
        initData()
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.gallery.collectLatest { adapter.submitData(it) }
                }
                launch {
                    adapter.loadStateFlow.collectLatest { loadState ->
                        val refresh = loadState.source.refresh
                        if (refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                            showEmpty(getString(R.string.empty_message))
                        } else if (refresh is LoadState.NotLoading) {
                            showContent()
                        } else if (refresh is LoadState.Loading) {
                            showLoading()
                        } else if (refresh is LoadState.Error) {
                            showError(refresh.error.toString())
                        } else {
                            showError(getString(R.string.error_message))
                        }
                    }
                }
            }
        }
    }

    private fun onItemClick(imageUrl: String) {
        startActivity(ImageActivity.getIntent(this, imageUrl))
    }

    private fun showLoading() {
        binding.apply {
            swipeRefresh.isRefreshing = true
            reloadButton.isVisible = false
            message.isVisible = false
            recyclerView.isVisible = true
        }
    }

    private fun showContent() {
        binding.apply {
            swipeRefresh.isRefreshing = false
            reloadButton.isVisible = false
            message.isVisible = false
            recyclerView.isVisible = true
        }
    }

    private fun showError(text: String?) {
        binding.apply {
            swipeRefresh.isRefreshing = false
            reloadButton.isVisible = true
            message.isVisible = true
            message.text = text
            recyclerView.isVisible = false
        }
    }

    private fun showEmpty(text: String?) {
        binding.apply {
            swipeRefresh.isRefreshing = false
            reloadButton.isVisible = false
            message.isVisible = true
            message.text = text
            recyclerView.isVisible = false
        }
    }
}
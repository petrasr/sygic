package com.example.sygic.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sygic.databinding.ItemStateGalleryBinding

class GalleryStateAdapter(
    private val retryClick: () -> Unit
) : LoadStateAdapter<GalleryStateAdapter.ItemStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemStateViewHolder {
        val view = ItemStateGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemStateViewHolder(view, retryClick)
    }

    override fun onBindViewHolder(holder: ItemStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class ItemStateViewHolder constructor(
        private val binding: ItemStateGalleryBinding,
        private val retryClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retryClick() }
        }

        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.NotLoading -> {
                    binding.errorView.isVisible = false
                    binding.retryButton.isVisible = false
                    binding.progressbar.isVisible = false
                }
                is LoadState.Loading -> {
                    binding.errorView.isVisible = false
                    binding.retryButton.isVisible = false
                    binding.progressbar.isVisible = true
                }
                is LoadState.Error -> {
                    binding.errorView.isVisible = true
                    binding.errorView.text = loadState.error.toString()
                    binding.retryButton.isVisible = true
                    binding.progressbar.isVisible = false
                }
            }
        }
    }
}
package com.example.sygic.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sygic.R
import com.example.sygic.data.model.GalleryItem
import com.example.sygic.databinding.ItemGalleryBinding

class GalleryAdapter(
    private val onItemClick: (imageUrl: String) -> Unit
) : PagingDataAdapter<GalleryItem, GalleryAdapter.ItemViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<GalleryItem>() {
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
                oldItem.url == newItem.url &&
                        oldItem.name == newItem.name &&
                        oldItem.origin == newItem.origin
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view) { adapterPosition ->
            getItem(adapterPosition)?.let {
                onItemClick(it.url)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ItemViewHolder constructor(
        private val binding: ItemGalleryBinding,
        private val onItemClick: (adapterPosition: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context: Context = binding.root.context

        init {
            binding.root.setOnClickListener { onItemClick(bindingAdapterPosition) }
        }

        fun bind(item: GalleryItem) {
            Glide
                .with(context)
                .load(item.url)
                .error(R.drawable.ic_baseline_error_24)
                .into(binding.imageView)

            binding.titleView.text = item.name
            binding.descriptionView.text = context.getString(
                R.string.gallery_item_description,
                item.origin
            )
        }
    }
}
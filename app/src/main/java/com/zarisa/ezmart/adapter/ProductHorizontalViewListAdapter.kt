package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ItemProductHorizontalBinding
import com.zarisa.ezmart.model.OnItemClick
import com.zarisa.ezmart.model.Product

class ProductHorizontalViewListAdapter(val onItemClick: OnItemClick) :
    ListAdapter<Product, ProductHorizontalViewListAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(
        private var binding: ItemProductHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.productImageSrc = if (product.images.isNotEmpty()) product.images[0].src else ""
            binding.root.setOnClickListener { onItemClick(product.id) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemProductHorizontalBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
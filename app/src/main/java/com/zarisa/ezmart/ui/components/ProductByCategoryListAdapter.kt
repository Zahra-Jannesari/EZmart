package com.zarisa.ezmart.ui.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ProductByCategoryItemBinding
import com.zarisa.ezmart.model.OnItemClick
import com.zarisa.ezmart.model.Product

class ProductByCategoryListAdapter(val onItemClick: OnItemClick) :
    ListAdapter<Product, ProductByCategoryListAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(
        private var binding: ProductByCategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.productImageSrc = product.images[0].src
            binding.root.setOnClickListener { onItemClick(product.id) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ProductByCategoryItemBinding.inflate(LayoutInflater.from(parent.context))
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
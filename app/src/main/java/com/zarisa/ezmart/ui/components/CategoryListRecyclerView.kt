package com.zarisa.ezmart.ui.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.CategoryListItemBinding
import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.OnCategoryClick

class CategoryListRecyclerView(var onCategoryClick: OnCategoryClick) :
    ListAdapter<Category, CategoryListRecyclerView.ViewHolder>(DiffCallback) {
    inner class ViewHolder(
        private var binding: CategoryListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.category = category
            binding.root.setOnClickListener { onCategoryClick(category) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            CategoryListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
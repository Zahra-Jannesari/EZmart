package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ItemShoppingCartBinding
import com.zarisa.ezmart.model.*

class CartListRecyclerViewAdapter(
    val deleteItem: OnDeleteOrderClick,
    val addOneItem: OnAddOrderItemClick,
    val removeOneItem: OnRemoveOrderItemClick
) :
    ListAdapter<CartItem, CartListRecyclerViewAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(
        private var binding: ItemShoppingCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.cartItem = item
            binding.productImageSrc =
                if (item.item.images.isNotEmpty()) item.item.images[0].src else ""
            binding.btnAddOne.setOnClickListener { addOneItem(item.item.id) }
            binding.btnRemoveOne.setOnClickListener { removeOneItem(item.item.id) }
            binding.btnDeleteItem.setOnClickListener { deleteItem(item.item.id) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemShoppingCartBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
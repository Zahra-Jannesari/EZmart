package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ItemShoppingCartBinding
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.shopping.ADD_ONE
import com.zarisa.ezmart.ui.shopping.DELETE_ITEM
import com.zarisa.ezmart.ui.shopping.REMOVE_ONE

class CartListRecyclerViewAdapter(
    val onEditCartItem: OnEditCartItem,
    val onItemClick: OnItemClick
) :
    ListAdapter<OrderItem, CartListRecyclerViewAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(
        private var binding: ItemShoppingCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.cartItem = item
            binding.productImageSrc = ""
//                if (item.item.images.isNotEmpty()) item.item.images[0].src else ""
            binding.btnAddOne.setOnClickListener { onEditCartItem(item.id, ADD_ONE) }
            binding.btnRemoveOne.setOnClickListener { onEditCartItem(item.id, REMOVE_ONE) }
            binding.btnDeleteItem.setOnClickListener { onEditCartItem(item.id, DELETE_ITEM) }
            binding.root.setOnClickListener { onItemClick(item.product_id) }
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

    companion object DiffCallback : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }
    }
}
package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ShoppingCartItemBinding
import com.zarisa.ezmart.model.*

class CartListRecyclerViewAdapter(
    val deleteItem: OnDeleteOrderClick,
    val addOneItem: OnAddOrderItemClick,
    val removeOneItem: OnRemoveOrderItemClick
) :
    ListAdapter<OrderItem, CartListRecyclerViewAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(
        private var binding: ShoppingCartItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.orderItem = item
            binding.btnAddOne.setOnClickListener { addOneItem(item.id) }
            binding.btnRemoveOne.setOnClickListener { removeOneItem(item.id) }
            binding.btnDeleteItem.setOnClickListener { deleteItem(item.id) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ShoppingCartItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
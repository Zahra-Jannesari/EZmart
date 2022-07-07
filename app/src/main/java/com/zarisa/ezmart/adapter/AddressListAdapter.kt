package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ListItem1Binding
import com.zarisa.ezmart.databinding.ListItem2Binding
import com.zarisa.ezmart.model.OnAddressClick

class AddressListAdapter(var onItemClick: OnAddressClick) :
    ListAdapter<String, RecyclerView.ViewHolder>(CityDiffCallback) {
    private val VIEW_TYPE_ONE = 1
    private val VIEW_TYPE_TWO = 2

    inner class Holder1(private val binding: ListItem1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            try {
                binding.tvAddress.text = item.split(",")[0]
                binding.root.setOnClickListener { onItemClick(item) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class Holder2(private val binding: ListItem2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            try {
                binding.tvAddress.text = item.split(",")[0]
                binding.root.setOnClickListener { onItemClick(item) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ONE) {
            Holder1(ListItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else Holder2(ListItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position % 2 == 0) {
            (holder as Holder1).bind(getItem(position))
        } else {
            (holder as Holder2).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            VIEW_TYPE_ONE
        } else VIEW_TYPE_TWO
    }
}

object CityDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
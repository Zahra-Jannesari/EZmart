package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ItemAttrListBinding
import com.zarisa.ezmart.model.AttrProps

class AttrListAdapter(var onAttrClick:(AttrProps)->Unit) :
    ListAdapter<AttrProps, AttrListAdapter.ViewHolder>(DiffCallback) {
    var currentAttrIndex = 0

    inner class ViewHolder(
        private var binding: ItemAttrListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attr: AttrProps, position: Int) {
            binding.attrName = attr.name
            binding.tvAttrName.isSelected = position == currentAttrIndex
            binding.root.setOnClickListener {
                currentAttrIndex = position
                binding.tvAttrName.isSelected = true
                onAttrClick(attr)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemAttrListBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct, position)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AttrProps>() {
        override fun areItemsTheSame(oldItem: AttrProps, newItem: AttrProps): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AttrProps, newItem: AttrProps): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
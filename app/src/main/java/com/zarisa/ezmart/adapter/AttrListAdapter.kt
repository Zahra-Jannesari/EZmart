package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ItemAttrBinding
import com.zarisa.ezmart.model.AttrProps

class AttrListAdapter(
    var onAttrClick: (AttrProps) -> Unit,
    var setItemBackground: (View, Boolean) -> Unit
) :
    ListAdapter<AttrProps, AttrListAdapter.ViewHolder>(DiffCallback) {
    var currentAttrIndex = 0
    private var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    inner class ViewHolder(
        private var binding: ItemAttrBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attr: AttrProps, position: Int) {
            binding.attrName = attr.name
            binding.tvAttrName.let {
                setItemBackground(binding.root, position == currentAttrIndex)
                it.setOnClickListener { _ ->
                    if (mRecyclerView != null && mRecyclerView?.isComputingLayout == false) {
                        currentAttrIndex = position
                        setItemBackground(binding.root, true)
                        onAttrClick(attr)
                        notifyDataSetChanged()
                    }
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemAttrBinding.inflate(LayoutInflater.from(parent.context))
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
package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ItemAttrTermsBinding
import com.zarisa.ezmart.model.AttrProps

class AttrTermsListAdapter(var onAttrClick: (AttrProps) -> Unit) :
    ListAdapter<AttrProps, AttrTermsListAdapter.ViewHolder>(DiffCallback) {
    var currentAttrIndex = -1
    private var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    inner class ViewHolder(
        private var binding: ItemAttrTermsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attr: AttrProps, position: Int) {
            binding.attrTermName = attr.name
            binding.radioBtnAttrTerm.isChecked = position == currentAttrIndex
            binding.radioBtnAttrTerm.setOnCheckedChangeListener { radioBtn, isChecked ->

                if (mRecyclerView != null && mRecyclerView?.isComputingLayout == false) {
                    currentAttrIndex = position
                    radioBtn.isChecked = isChecked
                    onAttrClick(attr)
                    notifyDataSetChanged()
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
            ItemAttrTermsBinding.inflate(LayoutInflater.from(parent.context))
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

    override fun onCurrentListChanged(
        previousList: MutableList<AttrProps>,
        currentList: MutableList<AttrProps>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        currentAttrIndex = -1
    }
}
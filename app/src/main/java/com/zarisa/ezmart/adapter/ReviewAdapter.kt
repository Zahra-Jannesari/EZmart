package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.ezmart.databinding.ItemReviewBinding
import com.zarisa.ezmart.model.OnDeleteReview
import com.zarisa.ezmart.model.OnEditReview
import com.zarisa.ezmart.model.Review

class ReviewAdapter(
    var userEmail: String?,
    var deleteReview: OnDeleteReview,
    var editReview: OnEditReview
) :
    ListAdapter<Review, ReviewAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(
        private var binding: ItemReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.review = review
            binding.lUserOptions.visibility = if (review.reviewer_email == userEmail) View.VISIBLE
            else View.GONE
            binding.btnEditReview.setOnClickListener{editReview(review)}
            binding.btnDeleteReview.setOnClickListener{deleteReview(review.id)}
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemReviewBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}
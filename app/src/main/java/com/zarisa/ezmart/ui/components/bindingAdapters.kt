package com.zarisa.ezmart.ui.components

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zarisa.ezmart.R
import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.Product


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imgView)
            .load(imgUrl)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.error_image)
            .into(imgView)
    }
}

@BindingAdapter("productListData")
fun bindProductRecyclerView(recyclerView: RecyclerView, data: List<Product>?) {
    val adapter = recyclerView.adapter as RecyclerViewAdapter
    adapter.submitList(data)
}

@BindingAdapter("categoryListData")
fun bindCategoryRecyclerView(recyclerView: RecyclerView, data: List<Category>?) {
    val adapter = recyclerView.adapter as CategoryListRecyclerView
    adapter.submitList(data)
}
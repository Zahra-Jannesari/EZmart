package com.zarisa.ezmart.ui.components

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zarisa.ezmart.R
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
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Product>?) {
    val adapter = recyclerView.adapter as ProductRecyclerViewAdapter
    adapter.submitList(data)
}
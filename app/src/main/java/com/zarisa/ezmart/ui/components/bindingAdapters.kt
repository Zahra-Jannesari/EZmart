package com.zarisa.ezmart.ui.components

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zarisa.ezmart.R


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
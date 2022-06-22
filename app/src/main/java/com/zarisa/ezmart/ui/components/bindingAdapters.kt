package com.zarisa.ezmart.ui.components

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zarisa.ezmart.R
import com.zarisa.ezmart.adapter.*
import com.zarisa.ezmart.model.*


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imgView)
            .load(imgUrl)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_error_image)
            .into(imgView)
    }
}

@BindingAdapter("productVerticalViewListData")
fun bindProductVerticalViewRecyclerView(recyclerView: RecyclerView, data: List<Product>?) {
    val adapter = recyclerView.adapter as ProductVerticalViewRecyclerViewAdapter
    adapter.submitList(data)
}

@BindingAdapter("reviewListData")
fun bindReviewsRecyclerView(recyclerView: RecyclerView, data: List<Review>?) {
    val adapter = recyclerView.adapter as ReviewAdapter
    adapter.submitList(data)
}

@BindingAdapter("productHorizontalViewListData")
fun bindProductHorizontalViewRecyclerView(recyclerView: RecyclerView, data: List<Product>?) {
    val adapter = recyclerView.adapter as ProductHorizontalViewListAdapter
    adapter.submitList(data)
}
@BindingAdapter("orderItemListData")
fun bindOrderItemRecyclerView(recyclerView: RecyclerView, data: List<OrderItem>?) {
    val adapter = recyclerView.adapter as CartListRecyclerViewAdapter
    adapter.submitList(data)
}
//@BindingAdapter("orderItemListData")
//fun bindOrderItemRecyclerView(recyclerView: RecyclerView, data: List<CartItem>?) {
//    val adapter = recyclerView.adapter as CartListRecyclerViewAdapter
//    adapter.submitList(data)
//}

@BindingAdapter("categoryList")
fun setCategories(textView: TextView, categoryList: List<Category>?) {
    if (!categoryList.isNullOrEmpty()) {
        textView.text = categoryList[0].name
        for (i in 1 until categoryList.size)
            textView.text = "${textView.text}/${categoryList[i].name}"
    }
}

@BindingAdapter("tagList")
fun setTags(textView: TextView, tagList: List<Tag>?) {
    if (!tagList.isNullOrEmpty()) {
        textView.text = "#${tagList[0].name}"
        for (i in 1 until tagList.size)
            textView.text = "${textView.text}#${tagList[i].name}"
    }
}

@BindingAdapter("setRate")
fun setRatingBarRate(ratingBar: RatingBar, rate: String?) {
    if (!rate.isNullOrBlank()) {
        ratingBar.rating = rate.toFloat()
    } else ratingBar.rating = 0F
}

@BindingAdapter("categoryListData")
fun bindCategoryRecyclerView(recyclerView: RecyclerView, data: List<Category>?) {
    val adapter = recyclerView.adapter as CategoryListRecyclerView
    adapter.submitList(data)
}

@BindingAdapter("setDescription")
fun setDescription(textView: TextView, description: String?) {
    textView.text =
        if (!description.isNullOrBlank())
            removeParagraphTags(description)?.let {
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
        else
            "جزییات بیشتری ثبت نشده است."
}

fun removeParagraphTags(input: String?): String? {
    return input?.replace("<p>".toRegex(), "")?.replace("</p>".toRegex(), "")
}

@BindingAdapter("setHtmlText")
fun setHtmlText(textView: TextView, description: String?) {
    textView.text =
        if (!description.isNullOrBlank())
            removeParagraphTags(description)?.let {
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
        else
            ""
}


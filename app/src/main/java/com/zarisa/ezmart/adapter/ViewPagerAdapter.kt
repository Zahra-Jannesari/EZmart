package com.zarisa.ezmart.adapter


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.zarisa.ezmart.R
import com.zarisa.ezmart.model.Image


class ViewPagerAdapter(private val sliderImgList: List<Image>, val context: Context) :
    PagerAdapter() {
    override fun getCount(): Int {
        return sliderImgList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context).apply {
            this.scaleType = ImageView.ScaleType.FIT_CENTER
            if (sliderImgList.isNotEmpty())
                sliderImgList[position].src.let {
                    Glide.with(this)
                        .load(it)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.error_image)
                        .into(this)
                }
        }
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }
}
package com.zarisa.ezmart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.zarisa.ezmart.R
import com.zarisa.ezmart.model.Image

class SliderAdapter(
    var fragment: Fragment,
    var imageList: MutableList<Image>,
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.imageSlider)

        fun setImage(url: String, imageViewItem: ImageView) {
            Glide.with(fragment)
                .load(url)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_error_image)
                .transform(CenterInside(), RoundedCorners(24))
                .into(imageViewItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(imageList[position].src, holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun newImageList(list: List<Image>) {
        imageList = list.toMutableList()
        notifyDataSetChanged()
    }
}
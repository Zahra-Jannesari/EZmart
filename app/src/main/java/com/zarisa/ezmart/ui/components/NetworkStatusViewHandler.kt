package com.zarisa.ezmart.ui.components

import android.view.View
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.LayoutNetworkStatusBinding
import com.zarisa.ezmart.model.NetworkStatus

class NetworkStatusViewHandler(
    status: NetworkStatus, viewMain: View, viewStatus: LayoutNetworkStatusBinding
) {
    init {
        when (status) {
            NetworkStatus.LOADING -> {
                viewMain.visibility = View.GONE
                viewStatus.tvNetworkStatus.visibility = View.GONE
                viewStatus.imageViewNetworkStatus.let { imageView ->
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(R.drawable.loading_animation)
                }
            }
            NetworkStatus.ERROR -> {
                viewMain.visibility = View.GONE
                viewStatus.tvNetworkStatus.visibility = View.VISIBLE
                viewStatus.imageViewNetworkStatus.let { imageView ->
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(R.drawable.network_error)
                }
            }
            else -> {
                viewMain.visibility = View.VISIBLE
                viewStatus.root.visibility = View.GONE
            }
        }
    }
}
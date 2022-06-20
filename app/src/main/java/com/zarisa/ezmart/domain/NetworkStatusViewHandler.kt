package com.zarisa.ezmart.domain

import android.view.View
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.LayoutNetworkStatusBinding
import com.zarisa.ezmart.model.Status

class NetworkStatusViewHandler(
    status: Status,
    viewMain: View,
    viewStatus: LayoutNetworkStatusBinding,
    onRefreshPageClick: (() -> Unit),
    message: String
) {
    init {
        when (status) {
            Status.LOADING -> {
                viewMain.visibility = View.GONE
                viewStatus.lStatus.visibility = View.GONE
                viewStatus.imageViewStatus.let { imageView ->
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(R.drawable.loading_animation)
                }
            }
            Status.NETWORK_ERROR -> {
                viewMain.visibility = View.GONE
                viewStatus.tvStatusMessage.text = message
                viewStatus.imageViewStatus.let { imageView ->
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(R.drawable.ic_no_internet)
                }
                viewStatus.lStatus.let { tv ->
                    tv.visibility = View.VISIBLE
                    tv.setOnClickListener {
                        onRefreshPageClick()
                    }
                }
            }
            Status.SUCCESSFUL -> {
                viewMain.visibility = View.VISIBLE
                viewStatus.root.visibility = View.GONE
            }
            else -> {
                viewMain.visibility = View.GONE
                viewStatus.tvStatusMessage.text = message
                viewStatus.lStatus.visibility = View.VISIBLE
                viewStatus.imageViewStatus.let { imageView ->
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(
                        if (message == REQUEST_NOT_FOUND) R.drawable.ic_search_off
                        else R.drawable.icon_server_error
                    )
                }
                viewStatus.lStatus.let { tv ->
                    tv.visibility = View.VISIBLE
                    tv.setOnClickListener {
                        onRefreshPageClick()
                    }
                }
            }
        }
    }
}
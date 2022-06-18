package com.zarisa.ezmart.ui.components

import android.view.View
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.LayoutNetworkStatusBinding
import com.zarisa.ezmart.model.Status
import com.zarisa.ezmart.ui.REQUEST_NOT_FOUND

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
                viewStatus.lWorkStatus.visibility = View.GONE
                viewStatus.imageViewNetworkStatus.let { imageView ->
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(R.drawable.loading_animation)
                }
            }
            Status.NETWORK_ERROR -> {
                viewMain.visibility = View.GONE
                viewStatus.lWorkStatus.let { tv ->
                    tv.visibility = View.VISIBLE
                    tv.setOnClickListener {
                        onRefreshPageClick()
                    }
                }
                viewStatus.tvStatusMessage.text = message
                viewStatus.imageViewNetworkStatus.let { imageView ->
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(R.drawable.ic_baseline_signal_wifi_statusbar_connected_no_internet_4_24)
                }
            }
            Status.SUCCESSFUL -> {
                viewMain.visibility = View.VISIBLE
                viewStatus.root.visibility = View.GONE
            }
            else -> {
                viewMain.visibility = View.GONE
                viewStatus.lWorkStatus.visibility = View.VISIBLE
                viewStatus.tvStatusMessage.text = message
                viewStatus.imageViewNetworkStatus.let { imageView ->
                    imageView.visibility = View.VISIBLE
                    imageView.setImageResource(if (message == REQUEST_NOT_FOUND) R.drawable.ic_baseline_search_off_24 else R.drawable.server_error)
                }
            }
        }
    }
}
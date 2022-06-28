package com.zarisa.ezmart.ui.detail

import android.os.CountDownTimer
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.zarisa.ezmart.R
import com.zarisa.ezmart.model.OrderingStatus

@BindingAdapter("setBtnState")
fun setToCartBtnState(button: MaterialButton, orderingStatus: OrderingStatus?) {
    when (orderingStatus) {
        OrderingStatus.LOADING_ORDER -> {
            button.let {
                it.setIconResource(R.drawable.ic_load_image)
                it.text = ""
                it.isEnabled = false
            }
        }
        OrderingStatus.ALREADY_ADDED -> {
            button.let {
                it.setIconResource(0)
                it.text = OrderingStatus.ALREADY_ADDED.message
                it.isEnabled = true
                it.isClickable = false
            }
        }
        OrderingStatus.ITEM_ADDED -> {
            button.let {
                it.setIconResource(0)
                it.text = OrderingStatus.ITEM_ADDED.message
                it.isEnabled = true
                it.isClickable = false
            }
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    button.text = OrderingStatus.ALREADY_ADDED.message
                }
            }.start()
        }
        OrderingStatus.ORDER_ERROR_INTERNET -> {
            button.let {
                it.setIconResource(R.drawable.ic_no_internet)
                it.text = OrderingStatus.ORDER_ERROR_INTERNET.message
                it.isEnabled = true
                it.isClickable = false
            }
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    button.setText(R.string.addToCart)
                    button.setIconResource(R.drawable.ic_shopping_cart)
                    button.isEnabled = true
                    button.isClickable = true
                }
            }.start()
        }
        OrderingStatus.ORDER_ERROR_SERVER -> {
            button.let {
                it.setIconResource(R.drawable.ic_server_error)
                it.text = OrderingStatus.ORDER_ERROR_SERVER.message
                it.isEnabled = true
                it.isClickable = false
            }
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    button.setText(R.string.addToCart)
                    button.setIconResource(R.drawable.ic_shopping_cart)
                    button.isClickable = true
                    button.isEnabled = true
                }
            }.start()
        }
        else -> {
            button.setIconResource(R.drawable.ic_shopping_cart)
            button.setText(R.string.addToCart)
            button.isClickable = true
        }
    }
}
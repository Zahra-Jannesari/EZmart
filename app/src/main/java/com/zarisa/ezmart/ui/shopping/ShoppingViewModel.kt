package com.zarisa.ezmart.ui.shopping

import androidx.lifecycle.ViewModel
import com.zarisa.ezmart.data.order.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {

}
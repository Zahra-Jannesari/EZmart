package com.zarisa.ezmart.ui.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.model.Order
import com.zarisa.ezmart.model.OrderItem
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val REMOVE_ONE = 101
const val ADD_ONE = 102
const val DELETE_ITEM = 103

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {
    var customerId = 0
    var orderId = MutableLiveData<Int>()
    val statusLiveData = MutableLiveData<Status>()
    val editCartStatus = MutableLiveData<Status?>(null)
    val orderItems = MutableLiveData<List<OrderItem>>()
    val total = MutableLiveData<String>()
    var statusMessage = ""
    val completeShoppingStatus = MutableLiveData<Status?>(null)
    val couponStatus = MutableLiveData<Status?>(null)

    fun getOrder() {
        statusLiveData.postValue(Status.LOADING)
        viewModelScope.launch {
            if (orderId.value == 0 || orderId.value == null) {
                statusLiveData.postValue(Status.EMPTY_CART)
                orderItems.postValue(emptyList())
            } else getOrderById()
        }
    }

    private suspend fun getOrderById() {
        customerRepository.retrieveOrder(orderId.value!!).let {
            statusMessage = it.message
            statusLiveData.value = it.status
            if (it.status == Status.SUCCESSFUL)
                it.data?.let { order ->
                    if (order.line_items.isNullOrEmpty())
                        statusLiveData.postValue(Status.EMPTY_CART)
                    else {
                        orderItems.value = order.line_items
                        total.value = order.total
                    }
                }
        }
    }

    fun updateOrder(itemId: Int, operation: Int) {
        viewModelScope.launch {
            editCartStatus.value = Status.LOADING
            customerRepository.retrieveOrder(orderId.value!!).let { resource ->
                if (resource.status == Status.SUCCESSFUL) {
                    val editedOrder = editOrder(resource.data, operation, itemId)
                    if (editedOrder != null) {
                        customerRepository.updateOrder(editedOrder, orderId.value!!).let {
                            if (it.status != Status.SUCCESSFUL)
                                editCartStatus.value = resource.status
                            else {
                                it.data?.let { order ->
                                    if (order.line_items.isNullOrEmpty())
                                        statusLiveData.postValue(Status.EMPTY_CART)
                                    else {
                                        orderItems.value = order.line_items
                                        total.value = order.total
                                    }
                                }
                            }
                        }
                    }
                } else editCartStatus.value = resource.status
            }
        }
    }

    private suspend fun getOrder(orderId: Int): Order? {
        customerRepository.retrieveOrder(orderId).let {
            return if (it.status == Status.SUCCESSFUL) it.data else null
        }
    }

    fun completeOrder() {
        completeShoppingStatus.postValue(Status.LOADING)
        viewModelScope.launch {
            val order = getOrder(orderId.value!!)
            if (order != null) {
                order.customer_id = customerId
                customerRepository.updateOrder(order, orderId.value!!).let {
                    completeShoppingStatus.postValue(it.status)
                    if (it.status == Status.SUCCESSFUL) {
                        orderId.postValue(0)
                    }
                }
            }
        }
    }

    private fun editOrder(order: Order?, operation: Int, itemId: Int): Order? {
        when (operation) {
            REMOVE_ONE -> order?.line_items?.onEach {
                if (it.id == itemId) {
                    it.quantity--
                    it.total = (Integer.parseInt(it.price) * it.quantity).toString()
                }
            }
            ADD_ONE -> order?.line_items?.onEach {
                if (it.id == itemId) {
                    it.quantity++
                    it.total = (Integer.parseInt(it.price) * it.quantity).toString()
                }
            }
            else -> order?.line_items?.onEach {
                if (it.id == itemId)
                    it.quantity = 0
            }
        }
        return order
    }

    fun checkCoupon(coupon: String) {
        couponStatus.postValue(Status.LOADING)
        viewModelScope.launch {
            customerRepository.retrieveCoupon(coupon).let {
                if (it.status == Status.SUCCESSFUL) {
                    if (it.data?.isNullOrEmpty() == false) {
                        customerRepository.retrieveOrder(orderId.value!!).let { order ->
                            if (order.status == Status.SUCCESSFUL) {
                                val newOrder = order.data
                                if (newOrder != null) {
                                    newOrder.coupon_lines = listOf(it.data!![0])
//                                    newOrder.discount_total = it.data!![0].discount
                                    customerRepository.updateOrder(newOrder, orderId.value!!)
                                        .let { update ->
                                            couponStatus.postValue(update.status)
                                            if (update.status == Status.SUCCESSFUL) {
                                                orderItems.value = update.data?.line_items
                                                total.value = update.data?.total
                                            }
                                        }
                                }
                            }else couponStatus.postValue(order.status)
                        }
                    } else couponStatus.postValue(Status.EMPTY_CART)
                } else couponStatus.postValue(it.status)
            }
        }
    }

    fun resetStatuses() {
        completeShoppingStatus.postValue(null)
        couponStatus.postValue(null)
    }
}
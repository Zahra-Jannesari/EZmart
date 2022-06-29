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
    var orderId = 0
    val statusLiveData = MutableLiveData<Status>()
    val editCartStatus = MutableLiveData<Status>()
    val orderItems = MutableLiveData<List<OrderItem>>()
    val total = MutableLiveData<String>()
    var statusMessage = ""

    fun getOrder() {
        statusLiveData.postValue(Status.LOADING)
        viewModelScope.launch {
            if (customerId == 0 && orderId == 0)
                statusLiveData.postValue(Status.EMPTY_CART)
            else if (customerId == 0)
                getOrderById()
            else getOrderByCustomerId()
        }
    }

    private suspend fun getOrderByCustomerId() {
        customerRepository.getCustomerOrders(customerId).let {
            statusMessage = it.message
            statusLiveData.value = it.status
            if (it.status == Status.SUCCESSFUL) {
                it.data?.let { orderList ->
                    if (orderList.isNullOrEmpty())
                        statusLiveData.postValue(Status.EMPTY_CART)
                    else {
                        orderItems.value = orderList[0].line_items
                        total.value = orderList[0].total
                    }
                }
            }
        }
    }

    private suspend fun getOrderById() {
        customerRepository.retrieveOrder(orderId).let {
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
            customerRepository.retrieveOrder(orderId).let { resource ->
                if (resource.status == Status.SUCCESSFUL) {
                    val editedOrder = editOrder(resource.data, operation, itemId)
                    if (editedOrder != null) {
                        customerRepository.updateOrder(editedOrder, orderId).let {
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
                } else {
                    editCartStatus.value = resource.status
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
}
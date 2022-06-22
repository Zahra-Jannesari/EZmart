package com.zarisa.ezmart.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {
    val orderingStatus = MutableLiveData<OrderingStatus?>(null)
    val statusLiveData = MutableLiveData<Status>()
    val abilityOfAddToCart = Transformations.map(statusLiveData) {
        it == Status.SUCCESSFUL
    }
    val currentProduct = MutableLiveData<Product?>()
    val reviewsList = MutableLiveData<List<Review>?>()
    var statusMessage = ""


    fun initialProduct(id: Int) {
        statusLiveData.value = Status.LOADING
        viewModelScope.launch {
            productRepository.getProductById(id).let {
                statusLiveData.value = it.status
                statusMessage = it.message
                if (it.status == Status.SUCCESSFUL) {
                    currentProduct.value = it.data
                    reviewsList.value = productRepository.getProductReviews(id).data
                }
            }
        }
    }


    //order part
    suspend fun createOrder(order: Order): Int {
        orderingStatus.postValue(OrderingStatus.LOADING_ORDER)
        customerRepository.createOrder(order).let {
            when (it.status) {
                Status.SUCCESSFUL -> {
                    orderingStatus.postValue(OrderingStatus.ITEM_ADDED)
                    return it.data!!.id
                }
                Status.NETWORK_ERROR -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_INTERNET)
                else -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_SERVER)
            }
            return 0
        }
    }

    fun updateOrder(orderId: Int, orderItem: OrderItem) {
        orderingStatus.postValue(OrderingStatus.LOADING_ORDER)
        viewModelScope.launch {
            val order = getOrder(orderId)
            if (order != null) {
                if (isItemAlreadyExist(order.line_items, orderItem.product_id)) {
                    orderingStatus.postValue(OrderingStatus.ALREADY_ADDED)
                } else {
                    val newList = mutableListOf<OrderItem>()
                    newList.addAll(order.line_items)
                    newList.add(orderItem)
                    order.line_items = newList
                    customerRepository.updateOrder(order, orderId).let {
                        when (it.status) {
                            Status.SUCCESSFUL -> orderingStatus.postValue(OrderingStatus.ITEM_ADDED)
                            Status.NETWORK_ERROR -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_INTERNET)
                            else -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_SERVER)
                        }
                    }
                }
            }
        }
    }

    private fun isItemAlreadyExist(itemList: List<OrderItem>, newItemId: Int): Boolean {
        var itemAlreadyExist = false
        for (i in itemList) {
            if (i.product_id == newItemId)
                itemAlreadyExist = true
        }
        return itemAlreadyExist
    }

    private suspend fun getOrder(orderId: Int): Order? {
        customerRepository.retrieveOrder(orderId).let {
            when (it.status) {
                Status.SUCCESSFUL -> return it.data
                Status.NETWORK_ERROR -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_INTERNET)
                else -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_SERVER)
            }
            return null
        }
    }
}
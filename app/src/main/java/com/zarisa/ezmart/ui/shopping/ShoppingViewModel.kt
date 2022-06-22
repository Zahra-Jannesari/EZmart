package com.zarisa.ezmart.ui.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.CartItem
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
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository
) :
    ViewModel() {
    var customerId = 0
    var orderId = 0
    val statusLiveData = MutableLiveData<Status>()
    val editCartStatus = MutableLiveData<Status>()

    //    val cartItemsLiveData = MutableLiveData<List<CartItem>>()
    val orderItems = MutableLiveData<List<OrderItem>>()
    val total = MutableLiveData<String>()
    val emptyCart = Transformations.map(orderItems) { it?.isNullOrEmpty() }
    var statusMessage = ""
    fun getOrder() {
        statusLiveData.postValue(Status.LOADING)
        viewModelScope.launch {
            if (customerId == 0)
                customerRepository.retrieveOrder(orderId).let {
                    statusLiveData.value = it.status
                    statusMessage = it.message
                    if (it.status == Status.SUCCESSFUL)
                        it.data?.let { order ->
                            orderItems.value = order.line_items
                            total.value = order.total
                        }
//                        prepareCartItemsForShow(it.data)
                }
            else customerRepository.getCustomerOrders(customerId).let {
                statusLiveData.value = it.status
                statusMessage = it.message
                if (it.status == Status.SUCCESSFUL)
                    it.data?.let { order ->
                        orderItems.value = order[0].line_items
                        total.value = order[0].total
                    }
//                    prepareCartItemsForShow(
//                        if (it.data?.isNullOrEmpty() == true) Order() else it.data?.get(0)
//                    )
            }
        }
    }

    /*
    private suspend fun prepareCartItemsForShow(order: Order?) {
            total.value = order?.total
            order?.line_items.let {
                if (!it.isNullOrEmpty())
                    for (i in it) {
                        productRepository.getCartProductById(i.product_id).let { resource ->
                            if (resource.status == Status.SUCCESSFUL) {
                                cartItemsLiveData.value.let { list ->
                                    if (list.isNullOrEmpty()) listOf(
                                        CartItem(
                                            resource.data!!,
                                            i.quantity
                                        )
                                    ) else list.plus(CartItem(resource.data!!, i.quantity))
                                }

                            } else {
                                statusLiveData.value = resource.status
                                statusMessage = resource.message
                                return
                            }
                        }
                    }
            }
        }
     */
    fun updateOrder(itemId: Int, operation: Int) {
        viewModelScope.launch {
            customerRepository.retrieveOrder(orderId).let { resource ->
                if (resource.status == Status.SUCCESSFUL) {
                    val updateOrder = resource.data
                    when (operation) {
                        REMOVE_ONE -> updateOrder?.line_items?.onEach {
                            if (it.id == itemId)
                                it.quantity++
                        }
                        ADD_ONE -> updateOrder?.line_items?.onEach {
                            if (it.id == itemId)
                                it.quantity--
                        }
                        else -> {
                            var index = 0
                            updateOrder?.line_items?.let {
                                for (i in it.indices)
                                    if (it[i].id == itemId) {
                                        index = i
                                    }
                            }
                            updateOrder?.line_items =
                                (updateOrder?.line_items)?.minus(updateOrder.line_items[index])!!
                        }
                    }
                    if (updateOrder != null) {
                        customerRepository.updateOrder(updateOrder, orderId).let {
                            if (it.status != Status.SUCCESSFUL)
                                editCartStatus.value = resource.status
                        }
                        customerRepository.retrieveOrder(orderId).let {
                            if (it.status == Status.SUCCESSFUL)
                                it.data?.let { order ->
                                    orderItems.value = order.line_items
                                    total.value = order.total
                                } else editCartStatus.value = resource.status
                        }
                    } else {
                        editCartStatus.value = resource.status
                    }
                }
            }
        }

    }
}
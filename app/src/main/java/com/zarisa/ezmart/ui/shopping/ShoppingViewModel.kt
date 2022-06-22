package com.zarisa.ezmart.ui.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.CartItem
import com.zarisa.ezmart.model.Order
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository
) :
    ViewModel() {
    var customerId = 0
    var orderId = 0
    var initialGetOrder = true
    val statusLiveData = MutableLiveData<Status>()
    val cartItemsLiveData = MutableLiveData<List<CartItem>>()
    val total = MutableLiveData<String>()
    val emptyCart = Transformations.map(cartItemsLiveData) { it?.isNullOrEmpty() }
    var statusMessage = ""
    fun getOrder() {
        if (initialGetOrder) {
            statusLiveData.postValue(Status.LOADING)
            initialGetOrder = false
        }
        viewModelScope.launch {
            if (customerId == 0)
                customerRepository.retrieveOrder(orderId).let {
                    statusLiveData.value = it.status
                    statusMessage = it.message
                    if (it.status == Status.SUCCESSFUL)
                        prepareCartItemsForShow(it.data)
                }
            else customerRepository.getCustomerOrders(customerId).let {
                statusLiveData.value = it.status
                statusMessage = it.message
                if (it.status == Status.SUCCESSFUL)
                    prepareCartItemsForShow(
                        if (it.data?.isNullOrEmpty() == true) Order() else it.data?.get(0)
                    )
            }
        }
    }

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

}
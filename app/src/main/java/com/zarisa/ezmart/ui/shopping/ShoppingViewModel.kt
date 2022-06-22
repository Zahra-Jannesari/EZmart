package com.zarisa.ezmart.ui.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.model.Order
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(private val customerRepository: CustomerRepository) :
    ViewModel() {
    val statusLiveData = MutableLiveData<Status>()
    val orderLiveData = MutableLiveData<Order?>()
    val emptyCart = Transformations.map(orderLiveData) {
        it?.line_items.isNullOrEmpty()
    }
    var statusMessage = ""
    fun getOrder(customerId: Int, orderId: Int) {
        statusLiveData.postValue(Status.LOADING)
        viewModelScope.launch {
            if (customerId == 0)
                customerRepository.retrieveOrder(orderId).let {
                    statusLiveData.value = it.status
                    statusMessage = it.message
                    if (it.status == Status.SUCCESSFUL)
                        orderLiveData.value = it.data
                }
            else
                customerRepository.getCustomerOrders(customerId).let {
                    statusLiveData.value = it.status
                    statusMessage = it.message
                    if (it.status == Status.SUCCESSFUL)
                        orderLiveData.value =
                            if (it.data?.isNullOrEmpty() == true) Order() else it.data?.get(0)
                }
        }
    }

}
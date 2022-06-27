package com.zarisa.ezmart.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val customerRepository: CustomerRepository) :
    ViewModel() {
    val customerLiveData = MutableLiveData<Customer?>()
    val statusLiveData = MutableLiveData<Status>()
    var statusMessage = ""
    fun getCustomer(customerId: Int) {
        statusLiveData.value = Status.LOADING
        viewModelScope.launch {
            customerRepository.getCustomer(customerId).let {
                if (it.status == Status.SUCCESSFUL)
                    customerLiveData.value = it.data
                else {
                    statusMessage = it.message
                    statusLiveData.value = it.status
                }
            }
        }
    }

    suspend fun createCustomer(customer: Customer): Int? {
        statusLiveData.value = Status.LOADING
        customerRepository.createCustomer(customer).let {
            statusMessage = it.message
            statusLiveData.value = it.status
            if (it.status == Status.SUCCESSFUL) {
                customerLiveData.postValue(it.data)
                return it.data?.id
            }
        }
        return null
    }


    fun updateOrder(customerId: Int, orderId: Int) {
        viewModelScope.launch {
            val order = getOrder(orderId)
            if (order != null) {
                order.customer_id = customerId
                customerRepository.updateOrder(order, orderId)
            }
        }
    }

    private suspend fun getOrder(orderId: Int): Order? {
        customerRepository.retrieveOrder(orderId).let {
            return if (it.status == Status.SUCCESSFUL) it.data else null
        }
    }
}

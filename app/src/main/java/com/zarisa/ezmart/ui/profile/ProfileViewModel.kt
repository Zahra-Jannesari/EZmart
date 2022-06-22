package com.zarisa.ezmart.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.OrderRepository
import com.zarisa.ezmart.model.Customer
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {
    val customerLiveData = MutableLiveData<Customer?>()
    val statusLiveData = MutableLiveData<Status>()
    var statusMessage = ""
    fun getCustomer(customerId: Int) {
        statusLiveData.value = Status.LOADING
        viewModelScope.launch {
            orderRepository.getCustomer(customerId).let {
                statusLiveData.value = it.status
                statusMessage = it.message
                if (it.status == Status.SUCCESSFUL)
                    customerLiveData.value = it.data
            }
        }
    }

    suspend fun createCustomer(customer: Customer): Int? {
        statusLiveData.value = Status.LOADING
        orderRepository.createCustomer(customer).let {
            statusLiveData.value = it.status
            statusMessage = it.message
            if (it.status == Status.SUCCESSFUL) {
                customerLiveData.postValue(it.data)
                return it.data?.id
            }
        }
        return null
    }
}
//728  730
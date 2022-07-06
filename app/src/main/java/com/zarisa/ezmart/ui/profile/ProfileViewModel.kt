package com.zarisa.ezmart.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.model.Customer
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val customerRepository: CustomerRepository) :
    ViewModel() {
    val customerLiveData = MutableLiveData<Customer?>(null)
    val statusLiveData = MutableLiveData<Status?>()
    var statusMessage = ""

    suspend fun createCustomer(customer: Customer): Customer? {
        statusLiveData.value = Status.LOADING
        customerRepository.createCustomer(customer).let {
            statusMessage = it.message
            statusLiveData.value = it.status
            if (it.status == Status.SUCCESSFUL) {
                customerLiveData.postValue(it.data)
                return it.data
            }
        }
        return null
    }
}

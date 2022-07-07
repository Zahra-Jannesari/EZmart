package com.zarisa.ezmart.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.model.Customer
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val customerRepository: CustomerRepository) :
    ViewModel() {
    var addressOne = MutableLiveData("")
    var addressTwo = MutableLiveData("")
    val customerLiveData = MutableLiveData<Customer?>(null)
    val statusLiveData = MutableLiveData<Status?>()
    var statusMessage = ""
    var newLatLong = ""
    val isRegistered = MutableLiveData(false)

    fun createCustomer(customer: Customer) {
        viewModelScope.launch {
            statusLiveData.value = Status.LOADING
            customerRepository.createCustomer(customer).let {
                statusMessage = it.message
                statusLiveData.value = it.status
                if (it.status == Status.SUCCESSFUL) {
                    customerLiveData.postValue(it.data)
                }
            }
        }
    }

    fun automaticLogin(customerId: Int) {
        viewModelScope.launch {
            statusLiveData.value = Status.LOADING
            customerRepository.getCustomer(customerId).let {
                statusMessage = it.message
                statusLiveData.value = it.status
                if (it.status == Status.SUCCESSFUL) {
                    customerLiveData.postValue(it.data)
                }
            }
        }
    }
}

package com.zarisa.ezmart.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zarisa.ezmart.model.NetworkStatus

open class ParentViewModel : ViewModel() {
    var networkStatusLiveData = MutableLiveData<NetworkStatus>()

    open fun setNetworkStatus(networkStatus: NetworkStatus) {
        networkStatusLiveData.value = networkStatus
    }
}
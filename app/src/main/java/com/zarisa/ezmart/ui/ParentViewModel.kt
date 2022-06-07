package com.zarisa.ezmart.ui

import androidx.lifecycle.MutableLiveData
import com.zarisa.ezmart.model.NetworkStatus

class ParentViewModel(){
    val networkStatus=MutableLiveData<NetworkStatus>()
}
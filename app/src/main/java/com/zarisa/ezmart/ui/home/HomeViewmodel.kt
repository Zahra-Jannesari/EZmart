package com.zarisa.ezmart.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    val newestProductsList = MutableLiveData<List<Product>>()
    val mostSeenProductsList = MutableLiveData<List<Product>>()
    val highRateProductsList = MutableLiveData<List<Product>>()

    fun getMainLists() {
        getNewestProducts()
        getMostSeenProducts()
        getHighRateProducts()
    }

    private fun getHighRateProducts() {
        TODO("Not yet implemented")
    }

    private fun getMostSeenProducts() {
        TODO("Not yet implemented")
    }

    private fun getNewestProducts() {
        TODO("Not yet implemented")
    }
}

